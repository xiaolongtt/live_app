package org.example.live.user.api.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.example.livebankinterface.Dto.SendGiftMqDto;
import com.example.livebankinterface.Rpc.IQiyuCurrencyAccountRpc;
import com.example.livegiftinterface.Dto.GiftConfigDTO;
import com.example.livegiftinterface.Interface.IGiftConfigRpc;
import com.example.webstarter.Error.ErrorAssert;
import com.example.webstarter.Interceptor.LiveRequestContext;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.example.live.common.interfaces.Topic.sendGiftServiceTopicName;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.example.live.user.api.Error.ApiErrorEnum;
import org.example.live.user.api.Service.IGiftService;
import org.example.live.user.api.Vo.Req.GiftReqVO;
import org.example.live.user.api.Vo.Resp.GiftConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 11:32
 * @注释
 */
@Service
public class IGiftServiceImpl implements IGiftService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IGiftServiceImpl.class);
    @DubboReference
    private IGiftConfigRpc giftConfigRpc;

    @DubboReference
    private IQiyuCurrencyAccountRpc currencyAccountRpc;

    @Resource
    private MQProducer mqProducer;

    @Override
    public List<GiftConfigVO> listGift() {
        return ConvertBeanUtils.convertList(giftConfigRpc.queryGiftList(), GiftConfigVO.class);
    }

    @Override
    public boolean sendGift(GiftReqVO giftReqVO) {
        GiftConfigDTO byGiftId = giftConfigRpc.getByGiftId(giftReqVO.getGiftId());
        ErrorAssert.isNotNull(byGiftId, ApiErrorEnum.SEND_GIFT_ERROR);
        //使用mq将扣除余额的操作变为异步实现
        SendGiftMqDto sendGiftMqDto=new SendGiftMqDto();
        sendGiftMqDto.setGiftId(giftReqVO.getGiftId());
        sendGiftMqDto.setUserId(LiveRequestContext.getUserID());
        sendGiftMqDto.setPrice(byGiftId.getPrice());
        sendGiftMqDto.setReceiverId(giftReqVO.getReceiverId());
        sendGiftMqDto.setRoomId(giftReqVO.getRoomId());
        sendGiftMqDto.setUuid(UUID.randomUUID().toString());
        //储存svgaUrl，便于前端进行礼物特效展示
        sendGiftMqDto.setUrl(byGiftId.getSvgaUrl());
        Message message=new Message();
        message.setTopic(sendGiftServiceTopicName.SEND_GIFT_SERVICE_TOPIC_NAME);
        message.setBody(JSON.toJSONBytes(sendGiftMqDto));

        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[gift-send] send result is {}", sendResult);
        } catch (Exception e) {
            LOGGER.info("[gift-send] send result is error:", e);
        }
        return true;
    }
}
