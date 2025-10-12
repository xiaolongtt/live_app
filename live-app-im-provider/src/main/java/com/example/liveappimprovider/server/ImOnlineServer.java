package com.example.liveappimprovider.server;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 20:42
 * @注释
 */
public interface ImOnlineServer {
    /**
     * 判断用户是否在线
     *
     * @param userId
     * @param appId
     * @return
     */
    boolean isOnline(long userId,int appId);
}
