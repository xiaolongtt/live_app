package com.example.liveappimcoreserver.Server;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/10 21:57
 * @注释
 */
public interface IMsgAckCheckService {
    /**
     * 主要是客户端发送ack包给到服务端后，调用进行ack记录的移除
     *
     * @param imMsgBodyDto
     */
    void doMsgAck(ImMsgBodyDto imMsgBodyDto);

    /**
     * 记录下消息的ack和times
     *
     * @param imMsgBodyDto
     * @param times
     */
    void recordMsgAck(ImMsgBodyDto imMsgBodyDto, int times);

    /**
     * 发送延迟消息，用于进行消息重试功能
     *
     * @param imMsgBodyDto
     */
    void sendDelayMsg(ImMsgBodyDto imMsgBodyDto);

    /**
     * 获取ack消息的重试次数
     *
     * @param msgId
     * @param userId
     * @param appId
     * @return
     */
    int getMsgAckTimes(String msgId,long userId,int appId);
}
