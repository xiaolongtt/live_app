package org.example.living.provider.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.liveappimcoreserverinterface.Dto.ImOfflineDTO;
import com.example.liveappimcoreserverinterface.Dto.ImOnlineDTO;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Enum.CommonStatusEnum;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.example.live.common.interfaces.Utils.PageWrapper;
import org.example.living.constants.LivingRoomRedisKey;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.dto.LivingRoomRespDTO;
import org.example.living.provider.Dao.Mapper.LivingRoomMapper;
import org.example.living.provider.Dao.Mapper.LivingRoomRecordMapper;
import org.example.living.provider.Dao.Po.LivingRoomPO;
import org.example.living.provider.Dao.Po.LivingRoomRecordPO;
import org.example.living.provider.Service.ILivingRoomService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:20
 * @注释
 */
@Service
public class livingRoomServiceImpl implements ILivingRoomService {
    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 主要用来在用户离开直播间后，将用户id从与roomId相关联的redis集合中移除
     * @param imOfflineDTO
     */
    @Override
    public void userLeaveLivingRoom(ImOfflineDTO imOfflineDTO) {
        Integer roomId = imOfflineDTO.getRoomId();
        Long userId = imOfflineDTO.getUserId();
        Integer appId = imOfflineDTO.getAppId();
        String key = LivingRoomRedisKey.LIVING_ROOM_USER_ENTER_KEY + roomId + ":" + appId;
        redisTemplate.opsForSet().remove(key,userId);
    }

    /**
     * 主要用来在用户进入直播间后，将用户id放入到与roomId相关联的redis集合中
     * @param imOnlineDTO
     */
    public void userEnterLivingRoom(ImOnlineDTO imOnlineDTO){
        Integer roomId = imOnlineDTO.getRoomId();
        Long userId = imOnlineDTO.getUserId();
        Integer appId = imOnlineDTO.getAppId();
        String key = LivingRoomRedisKey.LIVING_ROOM_USER_ENTER_KEY + roomId + ":" + appId;
        redisTemplate.opsForSet().add(key,userId,12,TimeUnit.HOURS);
    }

    /**
     * 开启直播间
     * @param livingRoomReqDTO
     * @return
     */
    @Override
    public Integer startingLiving(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomPO livingRoomPO = ConvertBeanUtils.convert(livingRoomReqDTO, LivingRoomPO.class);
        livingRoomPO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        livingRoomPO.setStartTime(new  Date());
        livingRoomMapper.insert(livingRoomPO);
        redisTemplate.delete(LivingRoomRedisKey.LIVING_ROOM_KEY+livingRoomPO.getId());
        return livingRoomPO.getId();
    }

    /**
     * 删除直播表中的记录，在直播记录表中插入一条记录，记录直播间的关闭时间
     * @param livingRoomReqDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean closingLiving(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomPO livingRoomPO = livingRoomMapper.selectById(livingRoomReqDTO.getRoomId());
        if(livingRoomPO==null){
            return false;
        }
        if(!livingRoomPO.getAnchorId().equals(livingRoomReqDTO.getAnchorId())){
            return false;
        }
        livingRoomMapper.deleteById(livingRoomReqDTO.getRoomId());
        LivingRoomRecordPO livingRoomRecordPO = ConvertBeanUtils.convert(livingRoomReqDTO, LivingRoomRecordPO.class);
        livingRoomRecordPO.setEndTime(new Date());
        livingRoomRecordPO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        livingRoomRecordMapper.insert(livingRoomRecordPO);
        redisTemplate.delete(LivingRoomRedisKey.LIVING_ROOM_KEY+livingRoomReqDTO.getRoomId());
        return true;
    }

    @Override
    public LivingRoomRespDTO queryByRoomId(Integer roomId) {
        //添加redis这一层
        LivingRoomRespDTO queryResult = (LivingRoomRespDTO) redisTemplate.opsForValue().get(LivingRoomRedisKey.LIVING_ROOM_KEY+roomId);
        if (queryResult != null) {
            //空值缓存
            if (queryResult.getId() == null) {
                return null;
            }
            return queryResult;
        }
        LambdaQueryWrapper<LivingRoomPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LivingRoomPO::getId, roomId);
        queryWrapper.eq(LivingRoomPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        queryResult=ConvertBeanUtils.convert(livingRoomMapper.selectOne(queryWrapper), LivingRoomRespDTO.class);
        if(Objects.isNull(queryResult)){
            //防止缓存击穿
            redisTemplate.opsForValue().set(LivingRoomRedisKey.LIVING_ROOM_KEY+roomId, new LivingRoomRespDTO(), 1, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(LivingRoomRedisKey.LIVING_ROOM_KEY+roomId, queryResult, 30, TimeUnit.MINUTES);
        return queryResult;
    }

    @Override
    public PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO) {
        int page = livingRoomReqDTO.getPage();
        int pageSize = livingRoomReqDTO.getPageSize();
        Long total = redisTemplate.opsForList().size(LivingRoomRedisKey.LIVING_ROOM_LIST_KEY);
        //先查看redis中是否有缓存
        List<Object> resultList = redisTemplate.opsForList().range(LivingRoomRedisKey.LIVING_ROOM_LIST_KEY, (long) (page - 1) * pageSize, ((long) page * pageSize));
        PageWrapper<LivingRoomRespDTO> pageWrapper = new PageWrapper<>();
        if (CollectionUtils.isEmpty(resultList)) {
            pageWrapper.setData(Collections.emptyList());
            pageWrapper.setHasNext(false);
            return pageWrapper;
        } else {
            List<LivingRoomRespDTO> livingRoomRespDTOS = ConvertBeanUtils.convertList(resultList, LivingRoomRespDTO.class);
            pageWrapper.setData(livingRoomRespDTOS);
            pageWrapper.setHasNext((long) page * pageSize < total);
            return pageWrapper;
        }
    }

    /**
     * 从数据库中查询全部的直播间列表
     * @param type
     * @return
     */
     @Override
    public List<LivingRoomRespDTO> selectAllFromDb(Integer type) {
        LambdaQueryWrapper<LivingRoomPO> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(LivingRoomPO::getStatus,CommonStatusEnum.VALID_STATUS);
        queryWrapper.eq(LivingRoomPO::getType,type);
        queryWrapper.orderByAsc(LivingRoomPO::getId);
        queryWrapper.last("limit 1000");
        List<LivingRoomPO> livingRoomPOS = livingRoomMapper.selectList(queryWrapper);
        return ConvertBeanUtils.convertList(livingRoomPOS, LivingRoomRespDTO.class);
    }

    /**
     * 根据直播间id查询直播间中的用户id列表
     * @param livingRoomReqDTO
     * @return
     */
     @Override
    public List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
         String key = LivingRoomRedisKey.LIVING_ROOM_USER_ENTER_KEY + livingRoomReqDTO.getRoomId() + ":" + livingRoomReqDTO.getAppId();
         //批次的从redis中获取用户数据
         Cursor<Object> scan = redisTemplate.opsForSet().scan(key, ScanOptions.scanOptions().match("*").count(100).build());
         List<Long> userIds = new ArrayList<>();
         while(scan.hasNext()){
             userIds.add((Long) scan.next());
         }
         return userIds;
     }

}
