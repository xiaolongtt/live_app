package com.example.livegiftprovider.Service.Impl;

import com.example.livegiftinterface.Dto.GiftRecordDTO;
import com.example.livegiftprovider.Dao.Mapper.GiftRecordMapper;
import com.example.livegiftprovider.Dao.Po.GiftRecordPO;
import com.example.livegiftprovider.Service.IGiftRecordService;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:53
 * @注释
 */
@Service
public class GiftRecordServiceImpl implements IGiftRecordService {
    @Resource
    private GiftRecordMapper giftRecordMapper;
    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        // 插入数据库
        GiftRecordPO giftRecordPO = ConvertBeanUtils.convert(giftRecordDTO, GiftRecordPO.class);
        giftRecordMapper.insert(giftRecordPO);
    }
}
