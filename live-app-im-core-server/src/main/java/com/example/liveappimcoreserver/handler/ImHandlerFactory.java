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
    void createHandler(ChannelHandlerContext ctx, ImMsg imMsg);
}
