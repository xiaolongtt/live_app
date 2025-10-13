package com.example.liveappimcoreserver.handler.Ws;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.ImHandlerFactory;
import com.example.liveappimcoreserver.handler.Impl.ImLogoutMsgHandlerImpl;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 11:15
 * @注释 im消息统一handler入口
 */
@ChannelHandler.Sharable //用于标记一个 ChannelHandler 可以被多个 ChannelPipeline 安全地共享
@Component
public class WsImCoreServerHandler extends SimpleChannelInboundHandler {

    @Resource
    private ImHandlerFactory imHandlerFactory;

    @Resource
    private ImLogoutMsgHandlerImpl logoutMsgHandler;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            wsMsgHandler(channelHandlerContext, (WebSocketFrame) msg);
        }
    }

    /**
     * 无论是正常下线还是掉线都会回到这个方法，可以用来进行通道的关闭和资源的释放
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId != null && appId != null) {
            //进行下线处理
            logoutMsgHandler.logoutHandler(ctx,userId,appId);
        }
    }

    private void wsMsgHandler(ChannelHandlerContext ctx, WebSocketFrame msg){
        //如果不是文本消息，统一后台不会处理
        if(!(msg instanceof TextWebSocketFrame)){
            throw new IllegalArgumentException("msg is not TextWebSocketFrame");
        }
        //返回应答消息
        String text = ((TextWebSocketFrame) msg).text();
        JSONObject jsonObject = JSON.parseObject(text, JSONObject.class);
        ImMsg imMsg=new ImMsg();
        imMsg.setMagic(jsonObject.getShortValue("magic"));
        imMsg.setLen(jsonObject.getIntValue("len"));
        imMsg.setCode(jsonObject.getIntValue("code"));
        imMsg.setBody(jsonObject.getString("body").getBytes());

        imHandlerFactory.createHandler(ctx,imMsg);
    }

}
