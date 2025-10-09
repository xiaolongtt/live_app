package com.example.liveappimcoreserver.handler.Impl;

import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:26
 * @注释 业务消息包：最常用的消息类型，例如我们的im收发数据
 */
@Component
public class ImSimpleMsgHandlerImpl implements SimpleHandler {

    @Resource
    private MQProducer mqProducer;
    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        //先进行基本的验证
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            ctx.close();
            throw new IllegalArgumentException("userId or appId is null");
        }
        //获取消息体
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            throw new IllegalArgumentException("body is null");
        }
        //要使用mq将消息传递给下游，也就是msg模块中，从队列中拿到数据去处理
        Message message=new Message();
        message.setBody(body);
        message.setTopic(ImCoreServerProviderTopicNames.LIVE_APP_IM_BIZ_MSG_TOPIC);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
