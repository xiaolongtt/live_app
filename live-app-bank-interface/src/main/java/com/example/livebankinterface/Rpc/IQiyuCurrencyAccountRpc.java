package com.example.livebankinterface.Rpc;

import com.example.livebankinterface.Dto.AccountTradeReqDTO;
import com.example.livebankinterface.Dto.AccountTradeRespDTO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:54
 * @注释
 */
public interface IQiyuCurrencyAccountRpc {
    /**
     * 新增账户
     *
     * @param userId
     */
    boolean insertOne(long userId);

    /**
     * 增加虚拟币
     *
     * @param userId
     * @param num
     */
    void incr(long userId,int num);

    /**
     * 扣减虚拟币
     *
     * @param userId
     * @param num
     */
    void decr(long userId,int num);

    /**
     * 查询余额
     *
     * @param userId
     * @return
     */
    Integer getBalance(Long userId);

    /**
     * 专门给送礼业务调用的扣减库存逻辑
     *
     * @param accountTradeReqDTO
     */
    AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO);
}
