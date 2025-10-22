package com.example.livebankinterface.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 15:37
 * @注释  账户交易响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTradeRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 247806292371212541L;
    private int code;
    private long userId;
    private boolean isSuccess;
    private String msg;
}
