package com.example.liveappimprovider.server;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 16:02
 * @注释
 */
public interface ImTokenServer {
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
