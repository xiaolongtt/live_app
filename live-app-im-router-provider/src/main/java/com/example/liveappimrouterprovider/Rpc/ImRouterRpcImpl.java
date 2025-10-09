package com.example.liveappimrouterprovider.Rpc;

import com.example.liveappiminterface.dto.ImMsgBodyDto;
import com.example.liveappimrouterinterface.Rpc.ImRouterRpc;
import com.example.liveappimrouterprovider.server.ImRouterService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 16:58
 * @注释
 */
@DubboService
public class ImRouterRpcImpl implements ImRouterRpc {
    @Resource
    private ImRouterService imRouterService;
    @Override
    public void sendMsg(ImMsgBodyDto msg) {
        imRouterService.sendMsg(msg);
    }
}
