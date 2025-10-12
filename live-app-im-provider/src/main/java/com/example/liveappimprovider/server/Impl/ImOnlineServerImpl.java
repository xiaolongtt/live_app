package com.example.liveappimprovider.server.Impl;

import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappimprovider.server.ImOnlineServer;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 20:42
 * @注释 主要是用来判断要进行对话的用户是否在线
 */
@Service
public class ImOnlineServerImpl implements ImOnlineServer {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 判断用户在某一个应用中是否在线
     * @param userId
     * @param appId
     * @return
     */
    @Override
    public boolean isOnline(long userId, int appId) {
        //在redis中查询，
        return redisTemplate.hasKey(ImCoreServerConstants.IM_BIND_IP_KEY + appId + userId);
    }
}
