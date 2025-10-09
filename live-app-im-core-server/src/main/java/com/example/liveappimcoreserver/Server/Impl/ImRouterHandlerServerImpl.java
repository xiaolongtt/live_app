package com.example.liveappimcoreserver.Server.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.Server.ImRouterHandlerServer;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 17:36
 * @注释
 */
@Service
public class ImRouterHandlerServerImpl implements ImRouterHandlerServer {
    /**
     * 这要是去找到需要进行消息通知的用户，然后将消息通知给用户的channel
     * @param imMsgBodyDto
     */
    @Override
    public void onReceive(ImMsgBodyDto imMsgBodyDto) {
        Long userId = imMsgBodyDto.getUserId();
        ChannelHandlerContext channel = ChannelHandlerContextCache.get(userId);
        if(channel!=null){
            ImMsg imMsgResp = ImMsg.build(ImCodeEnum.IM_MSG_BODY.getCode(), JSON.toJSONString(imMsgBodyDto));
            channel.writeAndFlush(imMsgResp);
        }
    }
}
