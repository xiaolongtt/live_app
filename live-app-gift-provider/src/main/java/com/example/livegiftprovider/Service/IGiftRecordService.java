package com.example.livegiftprovider.Service;

import com.example.livegiftinterface.Dto.GiftRecordDTO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:53
 * @注释
 */
public interface IGiftRecordService {
    /**
     * 插入一条礼物记录
     * @param giftRecordDTO
     */
    void insertOne(GiftRecordDTO giftRecordDTO);
}

