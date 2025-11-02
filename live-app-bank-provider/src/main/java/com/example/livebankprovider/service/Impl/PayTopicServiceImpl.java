package com.example.livebankprovider.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.livebankprovider.dao.Mapper.PayTopicMapper;
import com.example.livebankprovider.dao.Po.PayTopicPO;
import com.example.livebankprovider.service.IPayTopicService;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Enum.CommonStatusEnum;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 17:09
 * @注释
 */
@Service
public class PayTopicServiceImpl implements IPayTopicService {
    @Resource
    private PayTopicMapper payTopicMapper;
    @Override
    public PayTopicPO getByCode(Integer code) {
        LambdaQueryWrapper<PayTopicPO> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PayTopicPO::getBizCode,code);
        lambdaQueryWrapper.eq(PayTopicPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        lambdaQueryWrapper.last("limit 1");
        return payTopicMapper.selectOne(lambdaQueryWrapper);
    }
}
