package com.example.liveappimcoreserver.Rpc;

import com.example.liveappimcoreserver.Server.ImRouterHandlerServer;
import com.example.liveappimcoreserverinterface.rpc.ImRouterHandlerRpc;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 16:33
 * @注释 处理根据用户id转发消息的rpc调用
 */
@DubboService
public class ImRouterHandlerRpcImpl implements ImRouterHandlerRpc {
    @Resource
    private ImRouterHandlerServer imRouterHandlerServer;
    @Override
    public void routerMsg(ImMsgBodyDto imMsgBodyDto) {
        imRouterHandlerServer.onReceive(imMsgBodyDto);
    }
}
