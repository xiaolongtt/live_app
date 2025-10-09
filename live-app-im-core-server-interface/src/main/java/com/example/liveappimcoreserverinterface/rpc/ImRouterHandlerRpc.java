package com.example.liveappimcoreserverinterface.rpc;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 16:31
 * @注释 专门提供给router模块调用的rpc接口
 */
public interface ImRouterHandlerRpc {
    /**
     * 转发消息的接口
     * @param imMsgBodyDto 消息体
     */
    void routerMsg(ImMsgBodyDto imMsgBodyDto);
}
