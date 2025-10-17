package com.example.liveappimrouterprovider.server;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

import java.util.List;

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

    /**
     * 批量发送消息，主要是用于群播的场景
     * @param msgList
     */
    void batchSendMsg(List<ImMsgBodyDto> msgList);
}
