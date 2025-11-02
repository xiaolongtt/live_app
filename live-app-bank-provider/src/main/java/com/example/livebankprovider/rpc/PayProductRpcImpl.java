package com.example.livebankprovider.rpc;

import com.example.livebankinterface.Dto.PayProductDto;
import com.example.livebankinterface.Rpc.IPayProductRpc;
import com.example.livebankprovider.service.IPayProductService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/22 19:45
 * @注释
 */
@DubboService
public class PayProductRpcImpl implements IPayProductRpc {
    @Resource
    private IPayProductService payProductService;
    @Override
    public List<PayProductDto> getAllPayProducts(Integer type) {
        return payProductService.getAllPayProducts(type);
    }

    @Override
    public PayProductDto getProductById(Integer id) {
        return payProductService.getProductById(id);
    }
}
