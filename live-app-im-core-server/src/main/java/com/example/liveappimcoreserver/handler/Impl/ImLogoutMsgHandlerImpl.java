package com.example.liveappimcoreserver.handler.Impl;

import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:25
 * @注释 //登出消息包：正常断开im连接时发送的
 */
public class ImLogoutMsgHandlerImpl implements SimpleHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {

    }
}
