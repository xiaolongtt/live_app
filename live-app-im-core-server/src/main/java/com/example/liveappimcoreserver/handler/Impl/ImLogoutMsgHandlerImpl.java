package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:25
 * @注释 //登出消息包：正常断开im连接时发送的
 */
@Component
public class ImLogoutMsgHandlerImpl implements SimpleHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        //从channel中获取userId
        Long userId= ImContextUtils.getUserId(ctx);
        if(userId==null){
            throw new IllegalArgumentException("userId is null");
        }
        //将channel与userId从缓存中移除
        ChannelHandlerContextCache.remove(userId);
        ctx.close();
    }
}
