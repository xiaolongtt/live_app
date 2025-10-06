package com.example.liveappimcoreserver.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 20:52
 * @注释 用户获取channel中绑定的属性和根据属性获取对应的channel
 */
public class ImContextUtils {
    /**
     * 根据不同的channel获取到对应的id
     * @param ctx
     * @return
     */
    public static Long getUserId(ChannelHandlerContext ctx){
        return ctx.attr(ImContextAttr.USER_ID).get();
    }

    /**
     * 给对应的channel绑定id
     * @param userId
     * @param ctx
     */
    public static void setUserId(Long userId,ChannelHandlerContext ctx){
        ctx.attr(ImContextAttr.USER_ID).set(userId);
    }

    /**
     * 根据不同的channel获取到对应的id
     * @param ctx
     * @return
     */
    public static Integer getAppId(ChannelHandlerContext ctx){
        return ctx.attr(ImContextAttr.APP_ID).get();
    }

    /**
     * 给对应的channel绑定id
     * @param appid
     * @param ctx
     */
    public static void setAppId(Integer appid,ChannelHandlerContext ctx){
        ctx.attr(ImContextAttr.APP_ID).set(appid);
    }
}
