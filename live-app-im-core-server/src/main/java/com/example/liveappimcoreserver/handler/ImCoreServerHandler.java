package com.example.liveappimcoreserver.handler;

import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.Impl.ImHandlerFactoryImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 10:09
 * @注释 IM消息核心处理类，主要是根据消息的code字段进行路由到不同的处理方法
 */
public class ImCoreServerHandler extends SimpleChannelInboundHandler {

    private ImHandlerFactoryImpl imHandlerFactory=new ImHandlerFactoryImpl();
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(!(o instanceof ImMsg)){
            throw new IllegalArgumentException("o is not ImMsg");
        }
        ImMsg imMsg = (ImMsg) o;
        imHandlerFactory.createHandler(channelHandlerContext,imMsg);
    }
}
