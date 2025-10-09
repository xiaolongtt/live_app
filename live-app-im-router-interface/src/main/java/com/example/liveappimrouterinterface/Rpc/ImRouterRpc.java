package com.example.liveappimrouterinterface.Rpc;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 16:56
 * @注释
 */
public interface ImRouterRpc {
    /**
     * 发送消息
     * @param msg
     */
    void sendMsg(ImMsgBodyDto msg);
}
