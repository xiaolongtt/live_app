package com.example.livebankprovider.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.livebankinterface.Dto.PayOrderDto;
import com.example.livebankinterface.Dto.PayProductDto;
import com.example.livebankinterface.constants.OrderStatusEnum;
import com.example.livebankinterface.constants.PayProductTypeEnum;
import com.example.livebankprovider.dao.Mapper.PayOrderMapper;
import com.example.livebankprovider.dao.Po.PayOrderPO;
import com.example.livebankprovider.dao.Po.PayTopicPO;
import com.example.livebankprovider.service.*;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 19:46
 * @注释
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayOrderServiceImpl.class);
    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private IPayTopicService payTopicService;
    @Resource
    private IPayProductService payProductService;
    @Resource
    private IQiyuCurrencyAccountService currencyAccountService;
    @Resource
    private IQiyuCurrencyTradeService currencyTradeService;
    @Resource
    private MQProducer mqProducer;

    @Override
    public PayOrderPO queryByOrderId(String orderId) {
        LambdaQueryWrapper<PayOrderPO> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PayOrderPO::getOrderId,orderId);
        lambdaQueryWrapper.last("limit 1");
        return payOrderMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public String insertOne(PayOrderPO payOrderPO) {
        String orderID= UUID.randomUUID().toString();
        payOrderPO.setOrderId(orderID);
        payOrderMapper.insert(payOrderPO);
        return payOrderPO.getOrderId();
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        PayOrderPO payOrderPO=new PayOrderPO();
        payOrderPO.setId(id);
        payOrderPO.setStatus(status);
        return payOrderMapper.updateById(payOrderPO)>0;
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        PayOrderPO payOrderPO=queryByOrderId(orderId);
        if(payOrderPO==null){
            return false;
        }
        payOrderPO.setStatus(status);
        return payOrderMapper.updateById(payOrderPO)>0;
    }


    @Override
    public boolean payNotify(PayOrderDto payOrderDto) {
        PayOrderPO payOrderPO = this.queryByOrderId(payOrderDto.getOrderId());
        if (payOrderPO == null) {
            LOGGER.error("error payOrderPO, payOrderDTO is {}", payOrderDto);
            return false;
        }
        //当支付完成以后，可能会附带其它的操作，比如给用户发送消息，更新用户的vip积分等。
        //这里可以使用发送mq消息，让其它的服务来处理
        //但是对于topic的选择，如果这里是一个支付的中台处理，会有不同业务的支付请求，那么mq的消息topic也要对应不同的服务处理
        PayTopicPO payTopicPO = payTopicService.getByCode(payOrderDto.getBizCode());
        if (payTopicPO == null || StringUtils.isEmpty(payTopicPO.getTopic())) {
            LOGGER.error("error payTopicPO, payOrderDTO is {}", payOrderDto);
            return false;
        }
        boolean b = this.payNotifyHandler(payOrderDto);
        if(!b){
            return false;
        }
        String topic = payTopicPO.getTopic();
        Message message=new Message();
        message.setTopic(topic);
        message.setBody(JSON.toJSONString(payOrderPO).getBytes());
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(message);
            //TODO 增加处理消息的逻辑
            LOGGER.info("[payNotify] sendResult is {} ", sendResult);
        } catch (Exception e) {
            LOGGER.error("[payNotify] sendResult is {}, error is ", sendResult, e);
        }
        return true;
    }

    private boolean payNotifyHandler(PayOrderDto payOrderDto){
        //用户支付成功以后还要更新支付订单的状态和用户余额的修改
        this.updateOrderStatus(payOrderDto.getOrderId(), OrderStatusEnum.PAYED.getCode());
        //获取到用户购买的产品。根据产品的类型，来增加用户的余额。
        PayProductDto productById = payProductService.getProductById(payOrderDto.getProductId());
        if(productById!=null&& PayProductTypeEnum.QIYU_COIN.getCode().equals(productById.getType())){
            JSONObject jsonObject = JSON.parseObject(productById.getExtra());
            Integer num = jsonObject.getInteger("coin");
            currencyAccountService.incr(payOrderDto.getUserId(),num);
            return true;
        }
        return false;
    }
}
