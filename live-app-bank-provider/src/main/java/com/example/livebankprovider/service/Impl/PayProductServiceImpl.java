package com.example.livebankprovider.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.livebankinterface.Dto.PayProductDto;
import com.example.livebankinterface.constants.liveBankRedisKey;
import com.example.livebankprovider.dao.Mapper.PayProductMapper;
import com.example.livebankprovider.dao.Po.PayProductPO;
import com.example.livebankprovider.service.IPayProductService;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Enum.CommonStatusEnum;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/22 18:47
 * @注释
 */
@Service
public class PayProductServiceImpl implements IPayProductService {
    @Resource
    private PayProductMapper payProductMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<PayProductDto> getAllPayProducts(Integer type) {
        //从redis中获取支付产品列表
        String key = liveBankRedisKey.PAY_PRODUCT_KEY + type;
        List<PayProductDto> payProductDtos = redisTemplate.opsForList().range(key,0,-1).stream().map(x->(PayProductDto)x).toList();
        if (!CollectionUtils.isEmpty(payProductDtos)) {
            if(payProductDtos.get(0).getId()==null){
                return Collections.emptyList();
            }
            return payProductDtos;
        }
        LambdaQueryWrapper<PayProductPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayProductPO::getType, type);
        queryWrapper.eq(PayProductPO::getValidStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.orderByAsc(PayProductPO::getPrice);
        List<PayProductPO> payProductPOS = payProductMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(payProductPOS)){
            redisTemplate.opsForList().rightPushAll(key,new PayProductDto(),5, TimeUnit.MINUTES);
            return Collections.emptyList();
        }
        List<PayProductDto> dtos = ConvertBeanUtils.convertList(payProductPOS, PayProductDto.class);
        redisTemplate.opsForList().rightPushAll(key,dtos.toArray(),30, TimeUnit.MINUTES);
        return dtos;
    }

    @Override
    public PayProductDto getProductById(Integer id) {
        String key=liveBankRedisKey.PAY_PRODUCT_ITEM_KEY+id;
        PayProductPO poByRedis = (PayProductPO) redisTemplate.opsForValue().get(key);
        if(poByRedis!=null){
            if(poByRedis.getId()==null){
                return null;
            }
            return ConvertBeanUtils.convert(poByRedis, PayProductDto.class);
        }
        PayProductPO payProductPO = payProductMapper.selectById(id);
        if(Objects.isNull(payProductPO)){
            redisTemplate.opsForValue().set(key,new PayProductPO(),5, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(key,payProductPO,30, TimeUnit.MINUTES);
        return ConvertBeanUtils.convert(payProductPO, PayProductDto.class);
    }
}
