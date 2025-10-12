package com.example.liveappimcoreserver.Consumer;

import com.alibaba.fastjson2.JSON;
import com.example.liveappimcoreserver.Server.IMsgAckCheckService;
import com.example.liveappimcoreserver.Server.ImRouterHandlerServer;
import com.example.liveappimcoreserver.Server.Impl.IMsgAckCheckServiceImpl;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import com.example.mqstarter.properties.RocketMQConsumerProperties;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/11 14:50
 * @注释 来对确认消息进行消费
 */
public class ImAckConsumer implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Resource
    private ImRouterHandlerServer routerHandlerService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer=new DefaultMQPushConsumer();
        defaultMQPushConsumer.setVipChannelEnabled(false);
        defaultMQPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName()+"_"+ImAckConsumer.class.getSimpleName());
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.subscribe(ImCoreServerProviderTopicNames.LIVE_APP_IM_ACK_MSG_TOPIC, " ");
        defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            ImMsgBodyDto imMsgBodyDto = JSON.parseObject(list.get(0).getBody().toString(), ImMsgBodyDto.class);
            int ackTimes = msgAckCheckService.getMsgAckTimes(imMsgBodyDto.getMsgId(), imMsgBodyDto.getUserId(), imMsgBodyDto.getAppId());
            if(ackTimes<0){
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            //只支持一次重发
            if (ackTimes < 2) {
                //从新记录下ack次数
                msgAckCheckService.recordMsgAck(imMsgBodyDto, ackTimes + 1);
                msgAckCheckService.sendDelayMsg(imMsgBodyDto);
                routerHandlerService.sendMsgToClient(imMsgBodyDto);
            } else {
                msgAckCheckService.doMsgAck(imMsgBodyDto);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        defaultMQPushConsumer.start();
    }
}
