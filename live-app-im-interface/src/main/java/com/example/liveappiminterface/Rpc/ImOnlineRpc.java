package com.example.liveappiminterface.Rpc;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 20:41
 * @注释
 */
public interface ImOnlineRpc {
    /**
     * 判断用户是否在线
     *
     * @param userId
     * @param appId
     * @return
     */
    boolean isOnline(long userId,int appId);
}
