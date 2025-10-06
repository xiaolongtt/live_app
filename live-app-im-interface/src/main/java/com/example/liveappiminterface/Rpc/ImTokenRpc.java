package com.example.liveappiminterface.Rpc;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 15:59
 * @注释
 */
public interface ImTokenRpc {
    /**
     * 创建IM登录token
     * @param userId
     * @param appid
     * @return
     */
    String createTmLoginToken(long userId, int appid);

    /**
     * 根据token获取用户ID
     * @param token
     * @return
     */
    Long getUserIdByToken(String token);

}
