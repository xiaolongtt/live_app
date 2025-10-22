package com.example.livebankprovider.service.Impl;

import com.example.livebankprovider.dao.Mapper.QiyuCurrencyTradeMapper;
import com.example.livebankprovider.dao.Po.QiyuCurrencyTradePO;
import com.example.livebankprovider.service.IQiyuCurrencyTradeService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 15:07
 * @注释
 */
@Service
public class QiyuCurrencyTradeServiceImpl implements IQiyuCurrencyTradeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiyuCurrencyTradeServiceImpl.class);
    @Resource
    private QiyuCurrencyTradeMapper qiyuCurrencyTradeMapper;

    @Override
    public boolean insertOne(long userId, int num, int type) {
        try {
            QiyuCurrencyTradePO tradePO = new QiyuCurrencyTradePO();
            tradePO.setUserId(userId);
            tradePO.setNum(num);
            tradePO.setType(type);
            qiyuCurrencyTradeMapper.insert(tradePO);
            return true;
        } catch (Exception e) {
            LOGGER.error("[QiyuCurrencyTradeServiceImpl] insert error is:", e);
        }
        return false;
    }
}
