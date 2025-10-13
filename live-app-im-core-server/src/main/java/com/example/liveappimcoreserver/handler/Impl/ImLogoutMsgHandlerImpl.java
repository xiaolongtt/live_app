package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappimcoreserver.handler.Ws.WsSharkHandler;
import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:25
 * @注释 //登出消息包：正常断开im连接时发送的
 */
@Component
public class ImLogoutMsgHandlerImpl implements SimpleHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImLogoutMsgHandlerImpl.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        //从channel中获取userId
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        ImMsgBodyDto imMsgBodyDto=new ImMsgBodyDto();
        imMsgBodyDto.setUserId(userId);
        imMsgBodyDto.setAppId(appId);
        imMsgBodyDto.setData("success");
        ImMsg imMsgResp=ImMsg.build(ImCodeEnum.IM_LOGOUT.getCode(), JSON.toJSONString(imMsgBodyDto));
        ctx.writeAndFlush(imMsgResp);
        //将channel与userId从缓存中移除
        ChannelHandlerContextCache.remove(userId);
        //将userID与服务器ip的对应关系删除
        stringRedisTemplate.delete(ImCoreServerConstants.IM_BIND_IP_KEY+userId+appId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        ctx.close();
    }

    public void logoutHandler(ChannelHandlerContext ctx,Long userId,int appId){
        //将IM消息回写给客户端
        ImMsgBodyDto imMsgBodyDto=new ImMsgBodyDto();
        imMsgBodyDto.setUserId(userId);
        imMsgBodyDto.setAppId(appId);
        imMsgBodyDto.setData("success");
        ImMsg imMsgResp=ImMsg.build(ImCodeEnum.IM_LOGOUT.getCode(), JSON.toJSONString(imMsgBodyDto));
        ctx.writeAndFlush(imMsgResp);
        LOGGER.info("[LogoutMsgHandler] logout success, userId is {}, appId is {}", userId, appId);
        //handlerLogout(userId, appId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        ctx.close();
    }

}
