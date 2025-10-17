package com.example.livegiftprovider.Service;

import com.example.livegiftinterface.Dto.GiftConfigDTO;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:53
 * @注释
 */
public interface IGiftConfigService {
    /**
     * 按照礼物id查询
     *
     * @param giftId
     * @return
     */
    GiftConfigDTO getByGiftId(Integer giftId);

    /**
     * 查询所有礼物信息
     *
     * @return
     */
    List<GiftConfigDTO> queryGiftList();

    /**
     * 插入单个礼物信息
     *
     * @param giftConfigDTO
     */
    void insertOne(GiftConfigDTO giftConfigDTO);

    /**
     * 更新单个礼物信息
     *
     * @param giftConfigDTO
     */
    void updateOne(GiftConfigDTO giftConfigDTO);
}
