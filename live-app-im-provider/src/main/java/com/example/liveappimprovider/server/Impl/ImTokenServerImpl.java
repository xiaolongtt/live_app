package com.example.liveappimprovider.server.Impl;

import com.example.liveappimprovider.server.ImTokenServer;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 16:02
 * @注释
 */
@Service
public class ImTokenServerImpl implements ImTokenServer {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String createTmLoginToken(long userId, int appid) {
        String token= UUID.randomUUID()+"%"+appid;
        redisTemplate.opsForValue().set(token,userId,5, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        Object id = redisTemplate.opsForValue().get(token);
        if(id==null){
            return null;
        }
        return (Long) id;
    }
}
