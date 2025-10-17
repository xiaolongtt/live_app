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
     * 移除对应的userId
     * @param ctx
     */
    public static void removeUserId(ChannelHandlerContext ctx){
        ctx.attr(ImContextAttr.USER_ID).set(null);
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
    /**
     * 移除对应的appId
     * @param ctx
     */
    public static void removeAppId(ChannelHandlerContext ctx){
        ctx.attr(ImContextAttr.APP_ID).set(null);
    }

    public static Integer getRoomId(ChannelHandlerContext ctx) {
        return ctx.attr(ImContextAttr.ROOM_ID).get();
    }

    public static void setRoomId(ChannelHandlerContext ctx, int roomId) {
        ctx.attr(ImContextAttr.ROOM_ID).set(roomId);
    }
    /**
     * 移除对应的roomId
     * @param ctx
     */
    public static void removeRoomId(ChannelHandlerContext ctx){
        ctx.attr(ImContextAttr.ROOM_ID).set(null);
    }
}
