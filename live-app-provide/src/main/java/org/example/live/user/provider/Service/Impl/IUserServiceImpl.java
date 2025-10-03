package org.example.live.user.provider.Service.Impl;

import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.example.live.user.dto.UserDTO;
import org.example.live.user.provider.Service.IUserService;
import org.example.live.user.provider.dao.mapper.UserMapper;
import org.example.live.user.provider.dao.po.UserPO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/1/25 13:59
 * @注释
 */
@Service
public class IUserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String,UserDTO> redisTemplate;

    /**
     * 根据用户id查询，先从redis查询，没有再从数据库查询，将查询结果放入redis
     * @param userid
     * @return
     */
    @Override
    public UserDTO GetUserById(Long userid) {
        if(userid==null){
            return null;
        }
        String key="user_"+userid;
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if(userDTO!=null){
            return userDTO;
        }
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userid),UserDTO.class);
        if(userDTO!=null){
            redisTemplate.opsForValue().set(key,userDTO,30, TimeUnit.MINUTES);
        }
        return userDTO;
    }

    /**
     * 更新用户信息
     * @param userDTO
     * @return
     */
    @Override
    // TODO 这里差一个延迟双删，引入mq消息队列。设置一秒钟的延迟。先删除redis，更新数据库，再删除一次redis，这样可以保证数据的一致性
    public boolean updateUserInfo(UserDTO userDTO) {
        if(userDTO==null||userDTO.getUserId()==null){
            return false;
        }
        userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    /**
     * 插入用户信息
     * @param userDTO
     * @return
     */
    @Override
    public boolean insertUser(UserDTO userDTO) {
        if(userDTO==null||userDTO.getUserId()==null){
            return false;
        }
        userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    /**
     * 根据一批用户id查询用户信息
     * @param userIds
     * @return
     */
    @Override
    public Map<Long, UserDTO> GetUserByIds(List<Long> userIds) {
        if(userIds==null||userIds.isEmpty()){
            return null;
        }
        //先过滤无效id
        userIds=userIds.stream().filter(id->id>1000).collect(Collectors.toList());
        if(userIds.isEmpty()){
            return null;
        }
        //先进行redis查询
        List<String> keys=userIds.stream().map(id->"user_"+id).collect(Collectors.toList());
        List<UserDTO> userDTOINRedis = redisTemplate.opsForValue().multiGet(keys).stream().filter(userDTO->userDTO!=null).collect(Collectors.toList());
        if(!userDTOINRedis.isEmpty()&&userDTOINRedis.size()==userIds.size()){
            return userDTOINRedis.stream().collect(Collectors.toMap(UserDTO::getUserId,user->user));
        }
        //将redis查询到的id过滤掉
        List<Long> useridINRedis=userDTOINRedis.stream().map(UserDTO::getUserId).collect(Collectors.toList());
        List<Long> userIdNotINRedis=userIds.stream().filter(id->!useridINRedis.contains(id)).collect(Collectors.toList());
        //查询效率较低
        //userMapper.selectBatchIds(userIds);

        //先将id进行分组
        Map<Long,List<Long>> userMap=userIdNotINRedis.stream().collect(Collectors.groupingBy(id->id%10));
        //并行查询，提高效率
        List<UserDTO> DBQueryUserDTO=new CopyOnWriteArrayList<>();
        //parallelStream()并行处理数据流。用于并行执行集合上的操作。
        userMap.values().parallelStream().forEach(ids->{
            //并行处理10个id，查询效率较高
            DBQueryUserDTO.addAll(ConvertBeanUtils.convertList(userMapper.selectBatchIds(ids),UserDTO.class));
        });
         //将查询结果放入redis
        if(!DBQueryUserDTO.isEmpty()){
            Map<String, UserDTO> SaveRedisMap = DBQueryUserDTO.stream().collect(Collectors.toMap(user -> ("user_" + user.getUserId()), user -> user));
            redisTemplate.opsForValue().multiSet(SaveRedisMap);
            //通过管道设置过期时间，可以减少网络请求次数   executePipelined()方法适用于批量操作，将多个操作打包发送到Redis服务器，减少网络开销。
            redisTemplate.executePipelined(new SessionCallback<Object>(){
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for(String redisKey:SaveRedisMap.keySet()){
                        operations.expire((K) redisKey,getRandomNum(),TimeUnit.MINUTES);
                    }
                    return null;
                }
            });
        }
        userDTOINRedis.addAll(DBQueryUserDTO);
        return userDTOINRedis.stream().collect(Collectors.toMap(UserDTO::getUserId,user->user));
    }


    //生成一个10-100的随机数，使用随机函数设置过期时间，可以防止key同时过期
    private int getRandomNum(){
        return (int)(Math.random()*90+10);
    }
}
