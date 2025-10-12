package com.example.liveappimcoreserver.handler;

import com.example.liveappimcoreserver.common.ImMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:30
 * @注释
 */
public interface ImHandlerFactory {
    /**
     * 根据消息码创建对应的处理器，使用map来存储不同的处理类，根据code来分发不同的处理类
     * @param ctx
     * @param imMsg
     */
    void createHandler(ChannelHandlerContext ctx, ImMsg imMsg);
}
