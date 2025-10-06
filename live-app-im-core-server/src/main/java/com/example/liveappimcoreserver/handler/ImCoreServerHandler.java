package com.example.liveappimcoreserver.handler;

import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.Impl.ImHandlerFactoryImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 10:09
 * @注释 IM消息核心处理类，主要是根据消息的code字段进行路由到不同的处理方法
 */
@Component
public class ImCoreServerHandler extends SimpleChannelInboundHandler {

    @Resource
    private ImHandlerFactoryImpl imHandlerFactory;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(!(o instanceof ImMsg)){
            throw new IllegalArgumentException("o is not ImMsg");
        }
        ImMsg imMsg = (ImMsg) o;
        imHandlerFactory.createHandler(channelHandlerContext,imMsg);
    }

    /**
     * 无论是正常下线还是掉线都会回到这个方法，可以用来进行通道的关闭和资源的释放
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
