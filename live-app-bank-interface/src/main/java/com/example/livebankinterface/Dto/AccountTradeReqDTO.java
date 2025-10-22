package com.example.livebankinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 15:39
 * @注释 账户交易请求DTO
 */
@Data
public class AccountTradeReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3306156698114806876L;
    private long userId;
    //交易金额
    private int num;
}
