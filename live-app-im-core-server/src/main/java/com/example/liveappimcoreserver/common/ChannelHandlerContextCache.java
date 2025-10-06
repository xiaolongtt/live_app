package com.example.liveappimcoreserver.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 20:10
 * @注释 用一个map来缓存用户id与channel的映射关系
 */
public class ChannelHandlerContextCache {
    private static final Map<Long, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<>();
    public static ChannelHandlerContext get(long userid){
        return channelHandlerContextMap.get(userid);
    }
    public static void put(long userid,ChannelHandlerContext ctx){
        channelHandlerContextMap.put(userid,ctx);
    }
    public static void remove(long userid){
        channelHandlerContextMap.remove(userid);
    }
}

