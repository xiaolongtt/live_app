package com.example.liveappaccountprovider.rpc;

import com.example.liveappaccountinterface.IAccountTokenRPC;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/2 21:01
 * @注释
 */

@DubboService
public class AccountTokenRpcImpl implements IAccountTokenRPC {

    /**
     * 创建一个登录token并且储存到redis中
     *
     * @param userId
     * @return
     */
    @Override
    public String createAndSaveLoginToken(Long userId) {
        return null;
    }

    /**
     * 校验用户token
     *
     * @param tokenKey
     * @return
     */
    @Override
    public Long getUserIdByToken(String tokenKey) {
        return null;
    }
}
