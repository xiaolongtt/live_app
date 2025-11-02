package org.example.live.user.api.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.example.livebankinterface.Dto.PayOrderDto;
import com.example.livebankinterface.Dto.PayProductDto;
import com.example.livebankinterface.Rpc.IPayOrderRpc;
import com.example.livebankinterface.Rpc.IPayProductRpc;
import com.example.livebankinterface.Rpc.IQiyuCurrencyAccountRpc;
import com.example.livebankinterface.constants.OrderStatusEnum;
import com.example.livebankinterface.constants.PayChannelEnum;
import com.example.livebankinterface.constants.PaySourceEnum;
import com.example.webstarter.Error.BizBaseErrorEnum;
import com.example.webstarter.Error.ErrorAssert;
import com.example.webstarter.Interceptor.LiveRequestContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.live.user.api.Service.IBankService;
import org.example.live.user.api.Vo.Req.PayProductReqVO;
import org.example.live.user.api.Vo.Resp.PayProductItemVO;
import org.example.live.user.api.Vo.Resp.PayProductRespVO;
import org.example.live.user.api.Vo.Resp.PayProductVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/22 21:08
 * @注释
 */
public class IBankServiceImpl implements IBankService {
    @DubboReference
    private IPayProductRpc payProductRpc;
    @DubboReference
    private IQiyuCurrencyAccountRpc iQiyuCurrencyAccountRpc;

    @DubboReference
    private IPayOrderRpc payOrderRpc;

    @Override
    public PayProductVO products(Integer type) {
        List<PayProductDto> payProductDtos = payProductRpc.getAllPayProducts(type);
        List<PayProductItemVO> itemVOS = payProductDtos.stream().map(payProductDto -> {
            PayProductItemVO payProductItemVO = new PayProductItemVO();
            payProductItemVO.setId(payProductDto.getId());
            payProductItemVO.setName(payProductDto.getName());
            payProductItemVO.setCoinNum(JSON.parseObject(payProductDto.getExtra()).getInteger("coin"));
            return payProductItemVO;
        }).toList();
        PayProductVO payProductVO=new PayProductVO();
        payProductVO.setPayProductItemVOList(itemVOS);
        Integer balance = iQiyuCurrencyAccountRpc.getBalance(LiveRequestContext.getUserID());
        payProductVO.setCurrentBalance(balance);
        return payProductVO;
    }

    @Override
    public PayProductRespVO payProduct(PayProductReqVO payProductReqVO) {
        ErrorAssert.isNotNull(payProductReqVO, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(PaySourceEnum.find(payProductReqVO.getPaySource())!=null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(PayChannelEnum.find(payProductReqVO.getPayChannel())!=null, BizBaseErrorEnum.PARAM_ERROR);
        PayProductDto productById = payProductRpc.getProductById(payProductReqVO.getProductId());
        ErrorAssert.isNotNull(productById, BizBaseErrorEnum.PARAM_ERROR);
        //现在插入支付订单，订单状态默认为：0待支付
        PayOrderDto payOrderDto = new PayOrderDto();
        payOrderDto.setProductId(payProductReqVO.getProductId());
        payOrderDto.setUserId(LiveRequestContext.getUserID());
        payOrderDto.setSource(payProductReqVO.getPaySource());
        payOrderDto.setPayChannel(payProductReqVO.getPayChannel());
        String orderId = payOrderRpc.insertOne(payOrderDto);
        //更新订单为支付中状态
        payOrderRpc.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        PayProductRespVO payProductRespVO = new PayProductRespVO();
        payProductRespVO.setOrderId(orderId);
        //todo 远程http请求 restTemplate ==>支付回调接口


        return payProductRespVO;
    }
}
