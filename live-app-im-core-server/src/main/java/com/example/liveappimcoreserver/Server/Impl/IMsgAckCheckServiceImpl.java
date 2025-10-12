package com.example.liveappimcoreserver.Server.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.Server.IMsgAckCheckService;
import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/10 21:58
 * @注释 实现msg确认的服务。
 */
@Service
public class IMsgAckCheckServiceImpl implements IMsgAckCheckService {

    /**
     * Map来存储已发送但还未被确认的消息，键为消息ID，值为消息状态，还要定时检查是否超时未确认，超时后重新发送。
     */
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private MQProducer mqProducer;

    @Override
    public void doMsgAck(ImMsgBodyDto imMsgBodyDto) {
        redisTemplate.opsForHash().delete(ImCoreServerConstants.IM_MSG_ACK_KEY+imMsgBodyDto.getUserId()%100+":"+imMsgBodyDto.getAppId(),imMsgBodyDto.getMsgId());
    }

    @Override
    public void recordMsgAck(ImMsgBodyDto imMsgBodyDto, int times) {
        redisTemplate.opsForHash().put(ImCoreServerConstants.IM_MSG_ACK_KEY+imMsgBodyDto.getUserId()%100+":"+imMsgBodyDto.getAppId(),imMsgBodyDto.getMsgId(),times);
    }

    /**
     * 主要使用mq进行操作，设置延迟消息，判断5s延迟后是否收到客户端的ack确认，若未收到则重新发送一条延迟消息，直到收到确认或超过最大重试次数。
     * @param imMsgBodyDto
     */
    @Override
    public void sendDelayMsg(ImMsgBodyDto imMsgBodyDto) {
        Message message = new Message();
        message.setTopic(ImCoreServerProviderTopicNames.LIVE_APP_IM_ACK_MSG_TOPIC);
        //设置延迟时间等级为2，即5s后发送，等级1为1秒后发送
        message.setDelayTimeLevel(2);
        message.setBody(JSON.toJSONString(imMsgBodyDto).getBytes());
        try {
            SendResult sendResult = mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getMsgAckTimes(String msgId, long userId, int appId) {
        //获取到次数
        Integer times = (Integer) redisTemplate.opsForHash().get(ImCoreServerConstants.IM_MSG_ACK_KEY + userId % 100 + ":" + appId, msgId);
        return times==null?-1:times;
    }
}
