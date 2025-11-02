package com.example.livebankapi.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.livebankapi.Service.IPayNotifyService;
import com.example.livebankapi.Vo.WxPayNotifyVO;
import com.example.livebankinterface.Dto.PayOrderDto;
import com.example.livebankinterface.Rpc.IPayOrderRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 13:47
 * @注释
 */
@Service
public class IPayNotifyServiceImpl implements IPayNotifyService {
    @DubboReference
    private IPayOrderRpc iPayOrderRpc;
    @Override
    public String notifyHandler(String paramsJson) {
        WxPayNotifyVO wxPayNotifyVO = JSON.parseObject(paramsJson, WxPayNotifyVO.class);
        PayOrderDto payOrderDto=new PayOrderDto();
        payOrderDto.setUserId(wxPayNotifyVO.getUserId());
        payOrderDto.setOrderId(wxPayNotifyVO.getOrderId());
        payOrderDto.setBizCode(wxPayNotifyVO.getBizCode());
        return iPayOrderRpc.payNotify(payOrderDto)?"success":"fail";
    }
}
