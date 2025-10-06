package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:24
 * @注释 登录消息包：登录token验证，channel 和 userId 关联
 */
public class ImLoginMsgHandlerImpl implements SimpleHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        byte[] body = imMsg.getBody();
        //要将body转换未ImMsgBodyDto
        ImMsgBodyDto imMsgBodyDto = JSON.parseObject(new String(body), ImMsgBodyDto.class);
        //要先获取到Token。确保是登录消息
        String token = imMsgBodyDto.getToken();
        
    }
}
