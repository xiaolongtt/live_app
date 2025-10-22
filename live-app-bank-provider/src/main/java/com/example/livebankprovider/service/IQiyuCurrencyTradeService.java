package com.example.livebankprovider.service;

import com.example.livebankprovider.dao.Po.QiyuCurrencyTradePO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 15:07
 * @注释
 */
public interface IQiyuCurrencyTradeService {
    boolean insertOne(long userId, int num, int type);
}
