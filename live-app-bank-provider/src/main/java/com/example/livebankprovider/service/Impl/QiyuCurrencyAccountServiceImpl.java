package com.example.livebankprovider.service.Impl;

import com.example.livebankinterface.Dto.AccountTradeReqDTO;
import com.example.livebankinterface.Dto.AccountTradeRespDTO;
import com.example.livebankinterface.constants.TradeTypeEnum;
import com.example.livebankinterface.constants.liveBankRedisKey;
import com.example.livebankprovider.dao.Mapper.QiyuCurrencyAccountMapper;
import com.example.livebankprovider.dao.Po.QiyuCurrencyAccountPO;
import com.example.livebankprovider.service.IQiyuCurrencyAccountService;
import com.example.livebankprovider.service.IQiyuCurrencyTradeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:53
 * @注释
 */
@Service
public class QiyuCurrencyAccountServiceImpl implements IQiyuCurrencyAccountService {
    @Resource
    private QiyuCurrencyAccountMapper qiyuCurrencyAccountMapper;
    @Resource
    private IQiyuCurrencyTradeService currencyTradeService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    //创建一个线程池
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            2,
            3,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100)
    );
    @Override
    public boolean insertOne(long userId) {
        QiyuCurrencyAccountPO qiyuCurrencyAccountPO = new QiyuCurrencyAccountPO();
        qiyuCurrencyAccountPO.setUserId(userId);
        int i = qiyuCurrencyAccountMapper.insert(qiyuCurrencyAccountPO);
        return i > 0;
    }

    @Override
    public void incr(long userId, int num) {
        String key = liveBankRedisKey.ACCOUNT_BALANCE_KEY + ":" + userId;
        if(redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().increment(key,num);
            redisTemplate.expire(key,5, TimeUnit.MINUTES);
        }
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //分布式架构下，cap理论，可用性和性能，强一致性，柔弱的一致性处理
                //在异步线程池中完成数据库层的扣减和流水记录插入操作，带有事务
                incrBalanceByDb(userId, num);
            }
        });
    }

    @Override
    public void decr(long userId, int num) {
        //采用先更新redis中的值，再更新数据库中的值
        String key = liveBankRedisKey.ACCOUNT_BALANCE_KEY + ":" + userId;
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            redisTemplate.opsForValue().decrement(key,num);
            redisTemplate.expire(key,5, TimeUnit.MINUTES);
        }
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                decrBalanceByDb(userId, num);
            }
        });

    }

    @Override
    public Integer getBalance(long userId) {
        String key = liveBankRedisKey.ACCOUNT_BALANCE_KEY + ":" + userId;
        Integer balance = (Integer) redisTemplate.opsForValue().get(key);
        if(balance!=null){
            if(balance==-1){
                //空值缓存
                return null;
            }
            return balance;
        }
        QiyuCurrencyAccountPO qiyuCurrencyAccountPO = qiyuCurrencyAccountMapper.selectById(userId);
        if(qiyuCurrencyAccountPO == null){
            redisTemplate.opsForValue().set(key, -1, 5, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(key, qiyuCurrencyAccountPO.getCurrentBalance(),30, TimeUnit.MINUTES);
        return qiyuCurrencyAccountPO.getCurrentBalance();
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        long userId = accountTradeReqDTO.getUserId();
        int num = accountTradeReqDTO.getNum();
        //余额判断
        //获取用户余额
        Integer balance = this.getBalance(userId);
        if(balance==null||balance<num){
            //用户余额不足
            return new AccountTradeRespDTO(1,userId,false,"用户余额不足");
        }
        //用户余额充足，可以发送mq消息异步处理
        //流水记录
        //扣减余额
        this.decr(userId, num);
        return new AccountTradeRespDTO(0, userId,true,"扣款成功");
    }


    @Transactional(rollbackFor = Exception.class)
    public void decrBalanceByDb(long userId, int num) {
        QiyuCurrencyAccountPO qiyuCurrencyAccountPO = qiyuCurrencyAccountMapper.selectById(userId);
        if(qiyuCurrencyAccountPO == null){
            return;
        }
        qiyuCurrencyAccountPO.setCurrentBalance(qiyuCurrencyAccountPO.getCurrentBalance() - num);
        qiyuCurrencyAccountMapper.updateById(qiyuCurrencyAccountPO);
        //进行流水记录
        currencyTradeService.insertOne(userId, num*-1, TradeTypeEnum.SEND_GIFT_TRADE.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void incrBalanceByDb(long userId, int num) {
        QiyuCurrencyAccountPO qiyuCurrencyAccountPO = qiyuCurrencyAccountMapper.selectById(userId);
        if(qiyuCurrencyAccountPO == null){
            return;
        }
        qiyuCurrencyAccountPO.setCurrentBalance(qiyuCurrencyAccountPO.getCurrentBalance() + num);
        qiyuCurrencyAccountMapper.updateById(qiyuCurrencyAccountPO);
        //进行流水记录
        currencyTradeService.insertOne(userId, num, TradeTypeEnum.SEND_GIFT_TRADE.getCode());
    }
}
