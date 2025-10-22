package com.example.livebankinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:54
 * @注释
 */
@Data
public class QiyuCurrencyAccountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1270392385831310569L;

    private Long userId;
    private int currentBalance;
    private int totalCharged;
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
