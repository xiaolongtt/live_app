package com.example.liveappaccountprovider.service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/2 21:17
 * @注释
 */
public interface AccountTokenService {
    /**
     * 创建一个登录token并且储存到redis中
     *
     * @param userId
     * @return
     */
    String createAndSaveLoginToken(Long userId);

    /**
     * 校验用户token
     *
     * @param tokenKey
     * @return
     */
    Long getUserIdByToken(String tokenKey);
}
