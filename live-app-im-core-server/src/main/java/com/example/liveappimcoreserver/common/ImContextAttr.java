package com.example.liveappimcoreserver.common;

import io.netty.util.AttributeKey;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 20:49
 * @注释 用来给channel中绑定一些属性
 */
public class ImContextAttr {
    /**
     * 绑定用户id
     */
    public static AttributeKey<Long> USER_ID= AttributeKey.valueOf("userId");
    /**
     * 绑定应用id
     */
    public static AttributeKey<Integer> APP_ID= AttributeKey.valueOf("appId");

    /**
     * 绑定房间id
     */
    public static AttributeKey<Integer> ROOM_ID= AttributeKey.valueOf("roomId");

}
