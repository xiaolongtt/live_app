package com.example.livegiftinterface.Interface;

import com.example.livegiftinterface.Dto.GiftRecordDTO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:51
 * @注释
 */
public interface IGiftRecordRpc {

    /**
     * 插入一条礼物记录
     * @param giftRecordDTO
     */
    void insertOne(GiftRecordDTO giftRecordDTO);
}
