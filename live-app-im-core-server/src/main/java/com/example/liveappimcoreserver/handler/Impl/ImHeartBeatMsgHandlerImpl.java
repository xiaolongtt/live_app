package com.example.liveappimcoreserver.handler.Impl;

import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:26
 * @注释 心跳消息包：定时给im发送心跳包，验证im连接是否正常
 */
public class ImHeartBeatMsgHandlerImpl implements SimpleHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {

    }
}
