package com.example.livebankprovider.rpc;

import com.example.livebankinterface.Dto.AccountTradeReqDTO;
import com.example.livebankinterface.Dto.AccountTradeRespDTO;
import com.example.livebankinterface.Rpc.IQiyuCurrencyAccountRpc;
import com.example.livebankprovider.service.IQiyuCurrencyAccountService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:55
 * @注释
 */
@DubboService
public class QiyuCurrencyAccountRpcImpl implements IQiyuCurrencyAccountRpc {

    @Resource
    private IQiyuCurrencyAccountService qiyuCurrencyAccountService;

    @Override
    public boolean insertOne(long userId) {
        return qiyuCurrencyAccountService.insertOne(userId);
    }

    @Override
    public void incr(long userId, int num) {
        qiyuCurrencyAccountService.incr(userId,num);
    }

    @Override
    public void decr(long userId, int num) {
        qiyuCurrencyAccountService.decr(userId,num);
    }

    @Override
    public Integer getBalance(Long userId) {
        return qiyuCurrencyAccountService.getBalance(userId);
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        return qiyuCurrencyAccountService.consumeForSendGift(accountTradeReqDTO);
    }
}
