package com.example.livebankapi.Vo;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 13:46
 * @注释
 */
@Data
public class WxPayNotifyVO {
    private String orderId;
    private Long userId;
    /**
     * 用来区分业务类型
     */
    private Integer bizCode;
}
