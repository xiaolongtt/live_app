package com.example.liveappimcoreserver.handler;

import com.example.liveappimcoreserver.common.ImMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:22
 * @注释 不同类型的消息处理器都要实现这个接口
 */
public interface SimpleHandler {
    /**
     * 处理消息
     * @param ctx 通道上下文
     * @param imMsg 消息体
     */
    void handle(ChannelHandlerContext ctx, ImMsg imMsg);
}
