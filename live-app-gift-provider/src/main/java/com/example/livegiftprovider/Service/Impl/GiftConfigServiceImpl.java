package com.example.livegiftprovider.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.livegiftinterface.Dto.GiftConfigDTO;
import com.example.livegiftinterface.constants.GiftRedisKeyConstants;
import com.example.livegiftprovider.Dao.Mapper.GiftConfigMapper;
import com.example.livegiftprovider.Dao.Po.GiftConfigPO;
import com.example.livegiftprovider.Service.IGiftConfigService;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Enum.CommonStatusEnum;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:54
 * @注释
 */
@Service
public class GiftConfigServiceImpl implements IGiftConfigService {
    @Resource
    private GiftConfigMapper giftConfigMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public GiftConfigDTO getByGiftId(Integer giftId) {
        if(giftId==null){
            return null;
        }
        //先从redis中查询
        GiftConfigDTO giftConfigDTO = (GiftConfigDTO)redisTemplate.opsForValue().get(GiftRedisKeyConstants.GIFT_CONFIG_KEY + giftId);
        if(giftConfigDTO!=null){
            if(giftConfigDTO.getGiftId().equals(giftId)){
                redisTemplate.expire(GiftRedisKeyConstants.GIFT_CONFIG_KEY + giftId, 60, TimeUnit.MINUTES);
                return giftConfigDTO;
            }
            return null;
        }
        //如果redis中没有，则从数据库中查询
        LambdaQueryWrapper<GiftConfigPO> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(GiftConfigPO::getGiftId,giftId);
        queryWrapper.eq(GiftConfigPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        GiftConfigPO configPO = giftConfigMapper.selectOne(queryWrapper);
        if(configPO!=null){
            redisTemplate.opsForValue().set(GiftRedisKeyConstants.GIFT_CONFIG_KEY + giftId, ConvertBeanUtils.convert(configPO, GiftConfigDTO.class),60, TimeUnit.MINUTES);
            return ConvertBeanUtils.convert(configPO, GiftConfigDTO.class);
        }
        return null;
    }

    @Override
    public List<GiftConfigDTO> queryGiftList() {
        LambdaQueryWrapper<GiftConfigPO> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(GiftConfigPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        List<GiftConfigPO> configPOList = giftConfigMapper.selectList(queryWrapper);
        return ConvertBeanUtils.convertList(configPOList, GiftConfigDTO.class);
    }

    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        if(giftConfigDTO==null){
            return;
        }
        GiftConfigPO configPO = ConvertBeanUtils.convert(giftConfigDTO, GiftConfigPO.class);
        configPO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        giftConfigMapper.insert(configPO);
    }

    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        if(giftConfigDTO==null){
            return;
        }
        GiftConfigPO configPO = ConvertBeanUtils.convert(giftConfigDTO, GiftConfigPO.class);
        giftConfigMapper.updateById(configPO);
    }
}
