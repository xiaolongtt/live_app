package com.example.livebankprovider.dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 17:07
 * @注释
 */
@Data
@TableName("t_pay_topic")
public class PayTopicPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String topic;
    private Integer bizCode;
    private Integer status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}