package com.example.liveappimcoreserver.handler.Impl;

import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:26
 * @注释 业务消息包：最常用的消息类型，例如我们的im收发数据
 */
@Component
public class ImSimpleMsgHandlerImpl implements SimpleHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {

    }
}
