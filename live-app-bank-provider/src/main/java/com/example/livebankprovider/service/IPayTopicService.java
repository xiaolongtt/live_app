package com.example.livebankprovider.service;

import com.example.livebankprovider.dao.Po.PayTopicPO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 17:08
 * @注释
 */
public interface IPayTopicService {
    /**
     * 根据code查询
     *
     * @param code
     * @return
     */
    PayTopicPO getByCode(Integer code);
}
