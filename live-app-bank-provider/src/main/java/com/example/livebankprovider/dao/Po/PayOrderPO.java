package com.example.livebankprovider.dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;
/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:45
 * @注释 支付订单表
 */
@Data
@TableName("t_pay_order")
public class PayOrderPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderId;
    private Integer productId;
    private Long userId;
    private Integer source;
    private Integer payChannel;
    private Integer status;
    private Date payTime;
    private Date createTime;
    private Date updateTime;
}
