package com.example.livebankinterface.Rpc;

import com.example.livebankinterface.Dto.PayProductDto;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/22 19:45
 * @注释
 */
public interface IPayProductRpc {
    /**
     * 获取所有可用的支付商品
     * @param type 商品对应的业务类型
     * @return 所有可用的支付商品列表
     */
    List<PayProductDto> getAllPayProducts(Integer type);

    /**
     * 根据id获取支付商品详情
     * @param id 支付商品id
     * @return 支付商品详情
     */
    PayProductDto getProductById(Integer id);

}
