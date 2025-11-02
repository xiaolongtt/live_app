package com.example.livebankinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Savepoint;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 22:49
 * @注释
 */
@Data
public class PayOrderDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -5128379697967260665L;
    private Long id;
    private String orderId;
    /**
     * 用来区分业务类型
     */
    private Integer bizCode;
    private Integer productId;
    private Long userId;
    private Integer source;
    private Integer payChannel;
    private Integer status;
    private Date payTime;
    private Date createTime;
    private Date updateTime;
}
