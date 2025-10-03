package org.example.live.user.provider.Service.Impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.example.live.id.generate.Enum.IdTypeEnum;
import org.example.live.id.generate.interfaces.IdBuilderRpc;
import org.example.live.user.dto.UserDTO;
import org.example.live.user.dto.UserLoginDTO;
import org.example.live.user.dto.UserPhoneDTO;
import org.example.live.user.provider.Service.IUserPhoneService;
import org.example.live.user.provider.Service.IUserService;
import org.example.live.user.provider.dao.mapper.UserPhoneMapper;
import org.example.live.user.provider.dao.po.UserPhonePO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 21:40
 * @注释
 */
@Service
public class IUserPhoneServiceImpl implements IUserPhoneService {

    @Resource
    private UserPhoneMapper userPhoneMapper;
    @Resource
    private IUserService userService;
    @DubboReference
    private IdBuilderRpc idBuilderRpc;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    //储存token
    private static final String RedisTokenKey="Token";
    //储存手机号
    private static final String RedisPhoneKey="Phone";
    //储存手机号列表
    private static final String RedisPhoneListKey="PhoneList";

    /**
     * 用户手机号登录，用户不存在则注册创建。
     * @param phone
     * @return
     */
    @Override
    public UserLoginDTO login(String phone) {
        //判断是否为空
        if(phone==null){
            return UserLoginDTO.loginFail("手机号不能为空");
        }
        //判断是否以及注册过
        UserPhoneDTO userPhoneDTO =queryByPhone(phone);
        if(userPhoneDTO!=null){
            //注册过的话生成token返回
            return UserLoginDTO.loginSuccess(userPhoneDTO.getUserId(), createAndSaveToken(userPhoneDTO.getUserId()));
        }
        //没有注册进行注册并且登录
        return registerAndLoginUser(phone);
    }

    /**
     * 注册用户并登录
     * @param phone
     * @return
     */
    private UserLoginDTO registerAndLoginUser(String phone) {
        //使用分布式id生成器调用rpc接口生成用户id
        Long newUserId = idBuilderRpc.increaseUnSeqId(IdTypeEnum.USER_ID.getCode());
        //将手机号右移 2 位，然后取末尾两位数字
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(newUserId);
        userDTO.setNickName("用户-" + newUserId);
        userService.insertUser(userDTO);
        UserPhonePO userPhonePO = new UserPhonePO();
        userPhonePO.setUserId(newUserId);
        userPhonePO.setPhone(phone);
        userPhonePO.setStatus(1);
        userPhoneMapper.insert(userPhonePO);
        //记得删除空值缓存
        String key=RedisPhoneKey+phone;
        redisTemplate.delete(key);
        return UserLoginDTO.loginSuccess(newUserId, createAndSaveToken(newUserId));
    }

    /**
     * 创建并保存 token
     * @param userId
     * @return
     */
    private String createAndSaveToken(Long userId){
        //创建token
        String token = UUID.randomUUID().toString();
        String key=RedisTokenKey+token;
        //保存token到redis中,设置过期时间一天
        redisTemplate.opsForValue().set(key,userId,1, TimeUnit.DAYS);
        return token;
    }


    /**
     * 根据用户 id 查询用户手机号信息
     * @param userId
     * @return
     */
    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        if(userId==null){
            return Collections.emptyList();
        }
        //先在缓存中查询
        String key =RedisPhoneListKey+userId;
        List<Object> userPhonePOList = redisTemplate.opsForList().range(key,0,-1);
        //如果存在就直接返回
        if(userPhonePOList!=null){
            if(((UserPhonePO)userPhonePOList.get(0)).getUserId()==null){
                return Collections.emptyList();
            }
            return ConvertBeanUtils.convertList(userPhonePOList,UserPhoneDTO.class);
        }
        //否者从数据库中查询，并且将查询到的结果放入缓存中
        List<UserPhonePO> userPhonePOS = userPhoneMapper.selectByUserId(userId);
        //如果不存在就直接返回
        if(userPhonePOS!=null){
            //将查询到的结果放入缓存中
            redisTemplate.opsForList().rightPushAll(key,userPhonePOS,30,TimeUnit.MINUTES);
            return ConvertBeanUtils.convertList(userPhonePOS,UserPhoneDTO.class);
        }
        //缓存穿透，使用空值缓存
        //创建一个空值缓存，无论从数据库中是否查到都会设置一个缓存，防止缓存穿透，因为第二次的请求会先查到缓存中的空值提前返回。
        List<UserPhonePO> list = Arrays.asList(new UserPhonePO());
        redisTemplate.opsForList().rightPushAll(key,list,5,TimeUnit.MINUTES);
        return Collections.emptyList();
    }

    /**
     * 根据手机号查询用户手机号信息
     * @param phone
     * @return
     */
    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        if(phone==null){
            return null;
        }
        //先查询redis
        String key=RedisPhoneKey+phone;
        UserPhoneDTO userPhoneDTO = (UserPhoneDTO) redisTemplate.opsForValue().get(key);
        //存在就直接返回
        if(userPhoneDTO!=null ){
            //如果拿到的为空值缓存
            if(userPhoneDTO.getUserId()==null)
                return null;
            return userPhoneDTO;
        }
        //不存在就查询数据库，并且放入缓存中
        UserPhonePO userPhonePO = userPhoneMapper.selectByUserPhone(phone);
        if(userPhonePO!=null){
            userPhoneDTO=ConvertBeanUtils.convert(userPhonePO,UserPhoneDTO.class);
            //将查询到的结果放入缓存中
            redisTemplate.opsForValue().set(key,userPhoneDTO,30,TimeUnit.MINUTES);
            return userPhoneDTO;
        }
        //缓存击穿，使用空值缓存
        //创建一个空值缓存，无论从数据库中是否查到都会设置一个缓存，防止缓存击穿，因为第二次的请求会先查到缓存中的空值提前返回。
        redisTemplate.opsForValue().set(key,new UserPhoneDTO(),5,TimeUnit.MINUTES);
        return null;
    }
}
