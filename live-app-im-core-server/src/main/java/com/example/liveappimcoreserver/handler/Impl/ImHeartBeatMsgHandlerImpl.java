package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.constants.ImConstants;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:26
 * @注释 心跳消息包：定时给im发送心跳包，验证im连接是否正常
 */
@Component
public class ImHeartBeatMsgHandlerImpl implements SimpleHandler {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        //先对心跳包进行基本验证
        if(imMsg.getMagic()!= ImConstants.DEFAULT_MAGIC_NUMBER){
            ctx.close();
            throw new IllegalArgumentException("magic number is not correct");
        }
        byte[] body = imMsg.getBody();
        if(body==null||body.length==0){
            ctx.close();
            throw new IllegalArgumentException("body is null or empty");
        }
        ImMsgBodyDto imMsgBodyDto = JSON.parseObject(new String(body), ImMsgBodyDto.class);
        Long userId = imMsgBodyDto.getUserId();
        String token = imMsgBodyDto.getToken();
        int appId = imMsgBodyDto.getAppId();
        if(token==null||token.isEmpty() || userId<=1000 || appId<=10000){
            ctx.close();
            throw new IllegalArgumentException("token is null or empty");
        }
        //记录心跳包的record,使用redis来存储
        //对userId进行取模运算，分散储存
        String redisKey = ImConstants.REDIS_KEY_HEART_BEAT+userId%ImConstants.REDIS_ZSET_USER_ID_MOD+appId;
        this.recordHeartBeat(userId,redisKey);
        this.removeExpirerRecord(redisKey);
        redisTemplate.expire(redisKey,5, TimeUnit.MINUTES);
        //同时要更新用户id与服务器ip的对应时间
        stringRedisTemplate.expire(ImCoreServerConstants.IM_BIND_IP_KEY+userId+appId,5, TimeUnit.MINUTES);
        //返回数据包给客户端
        ImMsgBodyDto respImMsgBodyDto=new ImMsgBodyDto();
        respImMsgBodyDto.setUserId(userId);
        respImMsgBodyDto.setAppId(appId);
        respImMsgBodyDto.setData("success");
        ImMsg respImMsg = ImMsg.build(ImCodeEnum.IM_HEARTBEAT.getCode(), JSON.toJSONString(respImMsgBodyDto));
        ctx.writeAndFlush(respImMsg);
    }

    /**
     * 用来清理redis中超过30s还没有发送心跳的key
     * @param redisKey
     */
    private void removeExpirerRecord(String redisKey) {
        //根据key的时间值来清楚
        redisTemplate.opsForZSet().removeRangeByScore(redisKey,0,System.currentTimeMillis()-ImConstants.DEFAULT_HEART_BEAT_TIME*1000*2);
    }

    /**
     * 用zSet来进行存储，key(userId)---score(心跳时间);防止用户过多，对userId进行取模运算，分散储存
     * 在生成key的时候对userid进行取模，在确定key以后根据用户id获取到最新的心跳时间
     * 记录最近的一次心跳时间
     * @param userId
     * @param redisKey
     */
    private void recordHeartBeat(Long userId, String redisKey) {
        redisTemplate.opsForZSet().add(redisKey,userId,System.currentTimeMillis());
    }
}
