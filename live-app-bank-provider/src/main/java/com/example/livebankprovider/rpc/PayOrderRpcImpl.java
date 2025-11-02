package com.example.livebankprovider.rpc;

import com.example.livebankinterface.Dto.PayOrderDto;
import com.example.livebankinterface.Rpc.IPayOrderRpc;
import com.example.livebankprovider.dao.Po.PayOrderPO;
import com.example.livebankprovider.service.IPayOrderService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 22:51
 * @注释
 */
@DubboService
public class PayOrderRpcImpl implements IPayOrderRpc {

    @Resource
    private IPayOrderService payOrderService;

    @Override
    public PayOrderDto queryByOrderId(String orderId) {
        return ConvertBeanUtils.convert(payOrderService.queryByOrderId(orderId),PayOrderDto.class);
    }

    @Override
    public String insertOne(PayOrderDto payOrderDto) {
        return payOrderService.insertOne(ConvertBeanUtils.convert(payOrderDto, PayOrderPO.class));
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        return payOrderService.updateOrderStatus(id, status);
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        return payOrderService.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean payNotify(PayOrderDto payOrderDto) {
        return payOrderService.payNotify(payOrderDto);
    }
}
