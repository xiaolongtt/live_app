package com.example.liveappimprovider.Rpc;

import com.example.liveappiminterface.Rpc.ImTokenRpc;
import com.example.liveappimprovider.server.ImTokenServer;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 15:58
 * @注释
 */
@DubboService
public class ImTokenRpcImpl implements ImTokenRpc {

    @Resource
    private ImTokenServer imTokenServer;

    @Override
    public String createTmLoginToken(long userId, int appid) {
        return imTokenServer.createTmLoginToken(userId, appid);
    }

    @Override
    public Long getUserIdByToken(String token) {
        return imTokenServer.getUserIdByToken(token);
    }
}
