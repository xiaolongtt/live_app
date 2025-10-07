package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
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
        //先进行基本的验证
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            ctx.close();
            throw new IllegalArgumentException("userId or appId is null");
        }
        //获取消息体
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            throw new IllegalArgumentException("body is null");
        }
        //要使用mq将消息传递给下游

    }
}
