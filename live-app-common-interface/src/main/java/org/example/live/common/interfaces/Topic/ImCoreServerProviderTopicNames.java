package org.example.live.common.interfaces.Topic;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 15:50
 * @注释
 */
public class ImCoreServerProviderTopicNames {
    /**
     * 接收im系统发送的业务消息
     */
    public static final String LIVE_APP_IM_BIZ_MSG_TOPIC = "live_app_im_biz_msg_topic";

    /**
     * 发送im的ack消息
     */
    public static final String LIVE_APP_IM_ACK_MSG_TOPIC = "live_app_im_ack_msg_topic";

    /**
     * 用户初次登录im服务发送mq
     */
    public static final String IM_ONLINE_TOPIC = "im_online_topic";

    /**
     * 用户断开im服务发送mq
     */
    public static final String IM_OFFLINE_TOPIC = "im_offline_topic";
}
