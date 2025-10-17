package com.example.livegiftprovider.Rpc;

import com.example.livegiftinterface.Dto.GiftRecordDTO;
import com.example.livegiftinterface.Interface.IGiftRecordRpc;
import com.example.livegiftprovider.Service.IGiftRecordService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:52
 * @注释
 */
@DubboService
public class GiftRecordRpcImpl implements IGiftRecordRpc {

    @Resource
    private IGiftRecordService giftRecordService;
    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        giftRecordService.insertOne(giftRecordDTO);
    }
}
