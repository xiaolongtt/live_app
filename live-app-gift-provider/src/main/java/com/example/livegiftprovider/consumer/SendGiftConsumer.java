package com.example.livegiftprovider.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.liveappimcoreserverinterface.constants.ImMsgBizCodeEnum;
import com.example.liveappiminterface.constants.AppIdEnum;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import com.example.liveappimrouterinterface.Rpc.ImRouterRpc;
import com.example.livebankinterface.Dto.AccountTradeReqDTO;
import com.example.livebankinterface.Dto.AccountTradeRespDTO;
import com.example.livebankinterface.Dto.SendGiftMqDto;
import com.example.livebankinterface.Rpc.IQiyuCurrencyAccountRpc;
import com.example.livegiftinterface.constants.GiftRedisKeyConstants;
import com.example.mqstarter.properties.RocketMQConsumerProperties;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 20:31
 * @注释 作为发送礼物后进行扣款操作的消费者
 */
public class SendGiftConsumer implements InitializingBean {

    @DubboReference
    private IQiyuCurrencyAccountRpc currencyAccountRpc;
    @DubboReference
    private ImRouterRpc imRouterRpc;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RocketMQConsumerProperties rocketMqConsumerProperties;
    @Override
    public void afterPropertiesSet() throws Exception {
        //设置消费者的属性
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setConsumerGroup(rocketMqConsumerProperties.getGroupName()+"_"+SendGiftConsumer.class.getSimpleName());
        mqPushConsumer.setNamesrvAddr(rocketMqConsumerProperties.getNameSrv());
        //从通道中一次性获取10条消息消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //监听im发送来的消息topic
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.LIVE_APP_IM_BIZ_MSG_TOPIC , " ");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt messageExt : msgs) {
                SendGiftMqDto sendGiftMqDto = JSON.parseObject(messageExt.getBody(), SendGiftMqDto.class);
                String key = GiftRedisKeyConstants.SEND_GIFT_LOCK_KEY+sendGiftMqDto.getUuid();
                Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, -1, 5, TimeUnit.MINUTES);
                if(!lock){
                    //如果获取到锁失败，说明有其他消费者正在处理这个消息，直接跳过
                    continue;
                }
                AccountTradeReqDTO accountTradeReqDTO=new AccountTradeReqDTO();
                accountTradeReqDTO.setNum(sendGiftMqDto.getPrice());
                accountTradeReqDTO.setUserId(sendGiftMqDto.getUserId());
                //扣减用户余额
                AccountTradeRespDTO respDTO = currencyAccountRpc.consumeForSendGift(accountTradeReqDTO);
                ImMsgBodyDto imMsgBodyDto=new ImMsgBodyDto();
                imMsgBodyDto.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                JSONObject jsonObject=new JSONObject();
                //TODO 礼物特效全部可见
                if(respDTO.isSuccess()){
                    //扣款成功后，发送礼物特效
                    imMsgBodyDto.setUserId(sendGiftMqDto.getReceiverId());
                    imMsgBodyDto.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_SUCCESS.getCode());
                    jsonObject.put("url",sendGiftMqDto.getUrl());
                    imMsgBodyDto.setData(jsonObject.toJSONString());
                }else{
                    //扣款失败后，通过im服务发送给用户
                    imMsgBodyDto.setUserId(sendGiftMqDto.getUserId());
                    imMsgBodyDto.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_FAIL.getCode());
                    jsonObject.put("msg",respDTO.getMsg());
                    imMsgBodyDto.setData(jsonObject.toJSONString());
                }
                //发送im消息，将用户消息发送给前端，由前端进行特效展示或者通知余额不足
                imRouterRpc.sendMsg(imMsgBodyDto);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
    }
}
