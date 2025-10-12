package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson2.JSON;
import com.example.liveappimcoreserver.Server.IMsgAckCheckService;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/10 21:39
 * @注释 消息确认类，用来处理用户发送回来的ack消息
 */
public class AckImMsgHandlerImpl implements SimpleHandler {
    @Resource
    private IMsgAckCheckService imMsgAckCheckService;
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        //先取出用户id
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if(userId==null||appId==null){
            ctx.close();
            throw new IllegalArgumentException("user id or app id is null");
        }
        //收到确认消息后，删除该条记录
        imMsgAckCheckService.doMsgAck(JSON.parseObject(imMsg.getBody(), ImMsgBodyDto.class));
    }
}
