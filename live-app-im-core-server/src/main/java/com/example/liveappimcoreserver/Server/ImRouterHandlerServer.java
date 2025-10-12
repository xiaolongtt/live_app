package com.example.liveappimcoreserver.Server;

import com.example.liveappiminterface.dto.ImMsgBodyDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 17:36
 * @注释
 */
public interface ImRouterHandlerServer {
    void onReceive(ImMsgBodyDto imMsgBodyDto);

    boolean sendMsgToClient(ImMsgBodyDto imMsgBodyDto);
}
