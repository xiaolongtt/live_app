package com.example.livebankprovider.dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

//送礼物服务（用户的账户需要有一定的余额）
//通过一个接口，返回可以购买的产品列表
//映射我们的每个虚拟商品

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:45
 * @注释 支付商品表,用户能购买的产品列
 */
@TableName("t_pay_product")
@Data
public class PayProductPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer price;
    //主要用来映射该商品对应多少虚拟币以及储存对应支付二维码的url
    private String extra;
    private Integer type;
    private Integer validStatus;
    private Date createTime;
    private Date updateTime;
}
