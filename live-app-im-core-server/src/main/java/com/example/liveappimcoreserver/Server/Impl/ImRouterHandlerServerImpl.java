package com.example.liveappimcoreserver.Server.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.Server.IMsgAckCheckService;
import com.example.liveappimcoreserver.Server.ImRouterHandlerServer;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 17:36
 * @注释
 */
@Service
public class ImRouterHandlerServerImpl implements ImRouterHandlerServer {
    @Resource
    private IMsgAckCheckService imMsgAckCheckService;

    /**
     * 这要是去找到需要进行消息通知的用户，然后将消息通知给用户的channel
     * @param imMsgBodyDto
     */
    @Override
    public void onReceive(ImMsgBodyDto imMsgBodyDto) {
        if(sendMsgToClient(imMsgBodyDto)){
            //当服务器将消息发送给客户端后，记录消息发送的次数为1，当收到确认以后删除该记录
            imMsgAckCheckService.recordMsgAck(imMsgBodyDto, 1);
            imMsgAckCheckService.sendDelayMsg(imMsgBodyDto);
        }
    }

    @Override
    public boolean sendMsgToClient(ImMsgBodyDto imMsgBodyDto) {
        Long userId = imMsgBodyDto.getUserId();
        ChannelHandlerContext channel = ChannelHandlerContextCache.get(userId);
        if(channel!=null) {
            imMsgBodyDto.setMsgId(UUID.randomUUID().toString());
            ImMsg imMsgResp = ImMsg.build(ImCodeEnum.IM_MSG_BODY.getCode(), JSON.toJSONString(imMsgBodyDto));
            channel.writeAndFlush(imMsgResp);
            return true;
        }
        return false;
    }
}
