package com.example.liveappaccountprovider.service.impl;

import com.example.liveappaccountprovider.service.AccountTokenService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/2 21:17
 * @注释
 */
@Service
public class AccountTokenServiceImpl implements AccountTokenService {

    @Resource
    private RedisTemplate<String,Long> redisTemplate;

    private final String ACCOUNT_TOKEN_KEY="login_token";
    @Override
    public String createAndSaveLoginToken(Long userId) {
        //先生成token的字符串
        String token= UUID.randomUUID().toString();
        //要放入的redis中，key为token，value为userid
        String Key=ACCOUNT_TOKEN_KEY+token;
        redisTemplate.opsForValue().set(Key,userId,30, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        String Key=ACCOUNT_TOKEN_KEY+tokenKey;
        Long userId=redisTemplate.opsForValue().get(Key);
        if(userId==null){
            return null;
        }
        return userId;
    }
}
