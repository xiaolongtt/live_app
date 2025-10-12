package com.example.liveappimprovider.Rpc;

import com.example.liveappiminterface.Rpc.ImOnlineRpc;
import com.example.liveappimprovider.server.ImOnlineServer;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 20:40
 * @注释 主要是用来判断要进行对话的用户是否在线
 */
@DubboService
public class ImOnlineRpcIml implements ImOnlineRpc {
    @Resource
    private ImOnlineServer imOnlineServer;
    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineServer.isOnline(userId,appId);
    }
}
