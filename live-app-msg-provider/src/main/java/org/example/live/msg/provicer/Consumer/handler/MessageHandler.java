package org.example.live.msg.provicer.Consumer.handler;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 19:25
 * @注释 消息处理接口
 */
public interface MessageHandler {
    /**
     * 处理消息
     * @param imMsgBodyDto 消息体对象
     * @return 处理结果
     */
    void handleMsg(ImMsgBodyDto imMsgBodyDto);

}
