package com.example.liveappiminterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/5 15:30
 * @注释
 */
public class ImConstants {
    /**
     * 定义消息的魔法数
     */
    public static final short DEFAULT_MAGIC_NUMBER = 18673;
    /**
     * 定义redis中zSet存储userId的mod值
     */
    public static final int REDIS_ZSET_USER_ID_MOD = 10000;
    /**
     * 定义redis中存储心跳包的key
     */
    public static final String REDIS_KEY_HEART_BEAT = "im:heartbeat:userid+appid:";
    /**
     * 定义默认的客户端发送的心跳数据包间隔，单位为秒
     */
    public static final int DEFAULT_HEART_BEAT_TIME=30;
}


