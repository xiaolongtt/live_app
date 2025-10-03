package com.example.liveappaccountprovider.rpc;

import com.example.liveappaccountinterface.IAccountTokenRPC;
import com.example.liveappaccountprovider.service.AccountTokenService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/2 21:01
 * @注释
 */

@DubboService
public class AccountTokenRpcImpl implements IAccountTokenRPC {


    @Resource
    private AccountTokenService accountTokenService;

    /**
     * 创建一个登录token并且储存到redis中
     *
     * @param userId
     * @return
     */
    @Override
    public String createAndSaveLoginToken(Long userId) {
        return accountTokenService.createAndSaveLoginToken(userId);
    }

    /**
     * 校验用户token
     *
     * @param tokenKey
     * @return
     */
    @Override
    public Long getUserIdByToken(String tokenKey) {
        return accountTokenService.getUserIdByToken(tokenKey);
    }
}
