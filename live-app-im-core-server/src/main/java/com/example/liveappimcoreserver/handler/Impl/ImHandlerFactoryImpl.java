package com.example.liveappimcoreserver.handler.Impl;

import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.ImHandlerFactory;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappiminterface.constants.ImCodeEnum;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:32
 * @注释 这里是处理器工厂实现类，用于根据code来分发不同的处理类
 */
public class ImHandlerFactoryImpl implements ImHandlerFactory {

    /**
     * 处理消息的映射表，key为消息码，value为对应的处理类
     */
    private static final Map<Integer, SimpleHandler> handlerMap = new HashMap<>();

    static {
        handlerMap.put(ImCodeEnum.IM_LOGIN.getCode(), new ImLoginMsgHandlerImpl());
        handlerMap.put(ImCodeEnum.IM_LOGOUT.getCode(), new ImLogoutMsgHandlerImpl());
        handlerMap.put(ImCodeEnum.IM_MSG_BODY.getCode(), new ImSimpleMsgHandlerImpl());
        handlerMap.put(ImCodeEnum.IM_HEARTBEAT.getCode(), new ImHeartBeatMsgHandlerImpl());
    }

    @Override
    public void createHandler(ChannelHandlerContext ctx, ImMsg imMsg) {
        int code = imMsg.getCode();
        SimpleHandler handler = handlerMap.get(code);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for code: " + code);
        }
        handler.handle(ctx, imMsg);
    }
}
