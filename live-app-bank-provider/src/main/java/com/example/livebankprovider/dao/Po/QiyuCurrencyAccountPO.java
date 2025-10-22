package com.example.livebankprovider.dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:45
 * @注释
 */
@TableName("t_qiyu_currency_account")
@Data
public class QiyuCurrencyAccountPO {

    @TableId(type = IdType.INPUT)
    private Long userId;
    private int currentBalance;
    private int totalCharged;
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
