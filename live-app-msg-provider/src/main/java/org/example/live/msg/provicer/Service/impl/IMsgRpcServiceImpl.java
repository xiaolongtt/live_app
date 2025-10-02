package org.example.live.msg.provicer.Service.impl;

import jakarta.annotation.Resource;
import org.example.live.msg.constants.MsgSendResultEnum;
import org.example.live.msg.dto.MsgCheckDTO;
import org.example.live.msg.provicer.Dao.mapper.MsgMapper;
import org.example.live.msg.provicer.Dao.po.SmsPO;
import org.example.live.msg.provicer.Service.IMsgRpcService;
import org.example.live.msg.provicer.config.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 19:56
 * @注释
 */
@Service
public class IMsgRpcServiceImpl implements IMsgRpcService {

    private static Logger logger= LoggerFactory.getLogger(IMsgRpcServiceImpl.class);
    //用来储存验证码的key，有效期60s
    private static final String REDIS_KEY="msg_code:";

    @Resource
    private RedisTemplate<String,Integer> redisTemplate;

    @Resource
    private MsgMapper msgMapper;

    //发送短信验证码
    @Override
    public MsgSendResultEnum sendMessage(String phone) {
        if(StringUtils.isEmpty(phone)){
            return MsgSendResultEnum.MSG_PARAM_ERROR;
        }
        //生成6位随机的验证码，有效期为60s,不能在60s内重复发送，使用redis缓存60s
        String key=REDIS_KEY+phone;
        Boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey){
            logger.warn("手机号：{}，60s内不能重复发送",phone);
            return MsgSendResultEnum.SEND_FAIL;
        }
        //生成6位随机的验证码,存入redis中
        int code=(int)((Math.random()*9+1)*100000);
        redisTemplate.opsForValue().set(key,code,60, TimeUnit.SECONDS);
        //短信发送验证码（耗时一分钟）用异步线程处理
        ThreadPoolManager.commonAsyncThreadPool.execute(() -> {
            //TODO 短信发送验证码
            logger.info("开始发送短信，手机号：{}，验证码：{}",phone,code);
            insertOne(phone,code);
        });
        return MsgSendResultEnum.SEND_SUCCESS;
    }

    //校验登录验证码
    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        if(StringUtils.isEmpty(phone)||code==null){
            return new MsgCheckDTO(false,"参数异常");
        }
        String key=REDIS_KEY+phone;
        Integer code_redis = redisTemplate.opsForValue().get(key);
        if(code_redis==null){
            return new MsgCheckDTO(false,"验证码已过期");
        }
        if(!code_redis.equals(code)){
            return new MsgCheckDTO(false,"验证码错误");
        }
        //删除redis中的验证码
        redisTemplate.delete(key);
        return new MsgCheckDTO(true," ");
    }

    //将发送的验证码存入数据库中
    @Override
    public void insertOne(String phone, Integer code) {
        SmsPO sms=new SmsPO();
        sms.setPhone(phone);
        sms.setCode(code);
        msgMapper.insert(sms);
    }
}
