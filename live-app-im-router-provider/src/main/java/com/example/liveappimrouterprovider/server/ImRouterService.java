package com.example.liveappimrouterprovider.server;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 16:59
 * @注释
 */
public interface ImRouterService {
    /**
     * 发送消息
     * @param msg
     */
    void sendMsg(ImMsgBodyDto msg);
}
