package org.example.live.user.provider.Service.Impl;

import jakarta.annotation.Resource;
import org.example.live.common.interfaces.ConvertBeanUtils;
import org.example.live.user.constants.UserTagsEnum;
import org.example.live.user.dto.UserTagDTO;
import org.example.live.user.provider.Service.IUserTagService;
import org.example.live.user.provider.dao.mapper.UserTagMapper;
import org.example.live.user.provider.dao.po.UserTagPO;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static org.example.live.user.constants.UserTagFieldNameConstants.*;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/6 20:11
 * @注释
 */
@Service
public class IUserTagServiceImpl implements IUserTagService {
    @Resource
    private UserTagMapper userTagMapper;
    /**
     * 用来实现分布式锁
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 用来用户标签的缓存
     */
    @Resource
    private RedisTemplate<String, UserTagDTO> userTagDTORedisTemplate;

    /**
     * 分布式锁的名称
     */
    private static final String LOCK_NAME="userTagLock";
    /**
     * redis中用户标签的key
     */
    private static final String USER_TAG_KEY = "userTag:";
    /**
     *  设置标签,同时会更新数据库中的数据，那么redis中的数据为旧数据就要删除数据
     * @param userId
     * @param userTagsEnum
     * @return
     */
    //TODO 这里差一个延迟双删，引入mq消息队列。设置一秒钟的延迟。先删除redis，更新数据库，再删除一次redis，这样可以保证数据的一致性
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum)  {
        int result = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag());
        //影响的行数大于0，说明更新成功。否则更新失败要单独插入，使用分布式锁防止多个线程插入数据
        if(result>0){
            // 从redis中删除
            String key = USER_TAG_KEY + userId;
            userTagDTORedisTemplate.delete(key);
            return true;
        }

        /**
         * 可以用redis实现一个分布式锁，来保证只有一个线程可以插入数据，保证了数据库的安全。也可以使用Redis的组件Redisson来实现分布式锁。
         * (使用SETNX来实现，向redis中存入一个k-V,如果其他线程还要像里面存入，就会返回false,要记得删除这个k-V和设置过期时间)
         */
        String setAns = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                String key = LOCK_NAME + userId;
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                String SetResult = (String) connection.execute("SET",
                        keySerializer.serialize(key), valueSerializer.serialize("-1"),
                        "NX".getBytes(StandardCharsets.UTF_8),
                        "EX".getBytes(StandardCharsets.UTF_8),
                        "10".getBytes());
                return SetResult;
            }
        });
        //没有获取到分布式锁，不用你设置标签了，直接返回false
        if(!"OK".equals(setAns)){
            return false;
        }
        //获取到分布式锁进行插入操作
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        //如果数据库中没有数据，就插入数据。如有数据，就不用插入新的数据了，应为是其他线程已经插入了数据。
        if(userTagPO==null){
            userTagPO = new UserTagPO();
            userTagPO.setUserId(userId);
            String fieldName = userTagsEnum.getFieldName();
            if(TAG_INFO_01.equals(fieldName)){
                userTagPO.setTagInfo01(userTagsEnum.getTag());
            } else if (TAG_INFO_02.equals(fieldName)) {
                userTagPO.setTagInfo02(userTagsEnum.getTag());
            }else if (TAG_INFO_03.equals(fieldName)) {
                userTagPO.setTagInfo03(userTagsEnum.getTag());
            }
            userTagMapper.insert(userTagPO);
            // 插入成功后，要从redis中删除锁
            redisTemplate.delete(LOCK_NAME + userId);
            return true;
        }
        return false;
    }

    /**
     *  取消标签
     * @param userId
     * @param userTagsEnum
     * @return
     */
    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        int result = userTagMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag());
        if(!(result>0)){
            return false;
        }
        // 从redis中删除
        String key = USER_TAG_KEY + userId;
        userTagDTORedisTemplate.delete(key);
        return true;
    }

    /**
     *  判断用户是否有某个标签
     * @param userId
     * @param userTagsEnum
     * @return
     */
    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        // 先从redis中获取
        UserTagDTO userTagFromRedis =this.getUserTagFromRedis(userId);
        if(userTagFromRedis==null){
            return false;
        }
        String fieldName = userTagsEnum.getFieldName();
        if(TAG_INFO_01.equals(fieldName)){
            if((userTagFromRedis.getTagInfo01() & userTagsEnum.getTag()) == userTagsEnum.getTag()){
                return true;
            }
        } else if (TAG_INFO_02.equals(fieldName)) {
            if((userTagFromRedis.getTagInfo02() & userTagsEnum.getTag()) == userTagsEnum.getTag()){
                return true;
            }
        }else if (TAG_INFO_03.equals(fieldName)) {
            if((userTagFromRedis.getTagInfo03() & userTagsEnum.getTag()) == userTagsEnum.getTag()){
                return true;
            }
        }
        return false;
    }

    /**
     *  从redis中获取用户标签
     * @param userId
     * @return
     */
    private UserTagDTO getUserTagFromRedis(Long userId){
        String key = USER_TAG_KEY + userId;
        UserTagDTO userTagDTO = userTagDTORedisTemplate.opsForValue().get(key);
        if(userTagDTO!=null){
            return userTagDTO;
        }
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if(userTagPO==null){
            return null;
        }
        userTagDTO = ConvertBeanUtils.convert(userTagPO, userTagDTO.getClass());
        userTagDTORedisTemplate.opsForValue().set(key,userTagDTO);
        return userTagDTO;
    }
}
