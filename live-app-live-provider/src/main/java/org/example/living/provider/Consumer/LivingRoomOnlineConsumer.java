package org.example.living.provider.Consumer;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserverinterface.Dto.ImOnlineDTO;
import com.example.mqstarter.properties.RocketMQConsumerProperties;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.example.living.provider.Service.ILivingRoomService;
import org.springframework.beans.factory.InitializingBean;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 23:38
 * @注释
 */
public class LivingRoomOnlineConsumer implements InitializingBean {
    @Resource
    private RocketMQConsumerProperties rocketMqConsumerProperties;

    @Resource
    private ILivingRoomService livingRoomService;

    @Override
    public void afterPropertiesSet() throws Exception {
        //设置消费者的属性
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setConsumerGroup(rocketMqConsumerProperties.getGroupName()+"_"+LivingRoomOnlineConsumer.class.getSimpleName());
        mqPushConsumer.setNamesrvAddr(rocketMqConsumerProperties.getNameSrv());
        //从通道中一次性获取10条消息消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //监听im发送来的消息topic
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.IM_ONLINE_TOPIC , " ");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt messageExt : msgs) {
                ImOnlineDTO imOnlineDTO = JSON.parseObject(messageExt.getBody(), ImOnlineDTO.class);
                //在这里将用户id与roomId相关联，将用户id放入到与roomId相关联的redis集合中
                livingRoomService.userEnterLivingRoom(imOnlineDTO);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
    }
}
