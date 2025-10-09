package org.example.live.msg.provicer.Consumer;

import com.alibaba.fastjson.JSON;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import com.example.mqstarter.properties.RocketMQConsumerProperties;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.example.live.msg.provicer.Consumer.handler.MessageHandler;
import org.springframework.beans.factory.InitializingBean;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 18:08
 * @注释 主要用将用户发来的消息进行处理
 */
public class ImMsgConsumer implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMqConsumerProperties;

    @Resource
    private MessageHandler messageHandler;
    /**
     * 消息队列消费者初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //设置消费者的属性
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setConsumerGroup(rocketMqConsumerProperties.getGroupName()+"_"+ImMsgConsumer.class.getSimpleName());
        mqPushConsumer.setNamesrvAddr(rocketMqConsumerProperties.getNameSrv());
        //从通道中一次性获取10条消息消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //监听im发送来的消息topic
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.LIVE_APP_IM_BIZ_MSG_TOPIC , " ");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt messageExt : msgs) {
                ImMsgBodyDto imMsgBodyDto = JSON.parseObject(messageExt.getBody(), ImMsgBodyDto.class);
                messageHandler.handleMsg(imMsgBodyDto);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
    }
}
