package com.example.livebankinterface.Rpc;

import com.example.livebankinterface.Dto.PayOrderDto;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 22:48
 * @注释
 */
public interface IPayOrderRpc {
    /**
     * 根据订单id查询
     *
     * @param orderId
     */
    PayOrderDto queryByOrderId(String orderId);

    /**
     * 插入订单，返回订单id
     *
     * @param payOrderPO
     */
    String insertOne(PayOrderDto payOrderPO);


    /**
     * 根据主键id做更新
     *
     * @param id
     * @param status
     */
    boolean updateOrderStatus(Long id,Integer status);

    /**
     * 根据订单id做更新
     *
     * @param orderId
     * @param status
     */
    boolean updateOrderStatus(String orderId,Integer status);

    /**
     * 支付成功，更新订单状态
     * 支付回调需要请求该接口
     * @param payOrderDto
     */
    boolean payNotify(PayOrderDto payOrderDto);
}
