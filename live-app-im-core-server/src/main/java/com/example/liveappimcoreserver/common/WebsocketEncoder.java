package com.example.liveappimcoreserver.common;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 16:25
 * @注释 编码器 用于将ImMsg对象转换为TextWebSocketFrame对象
 */
public class WebsocketEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ImMsg)) {
            super.write(ctx, msg, promise);
            return;
        }
        ImMsg imMsg = (ImMsg) msg;
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(imMsg)));
    }
}
