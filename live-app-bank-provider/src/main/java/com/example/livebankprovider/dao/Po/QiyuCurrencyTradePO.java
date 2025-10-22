package com.example.livebankprovider.dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 15:05
 * @注释
 */
@TableName("t_qiyu_currency_trade")
@Data
public class QiyuCurrencyTradePO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer num;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
