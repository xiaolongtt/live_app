package com.example.livegiftprovider.Dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:48
 * @注释
 */

@TableName("t_gift_record")
@Data
public class GiftRecordPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long objectId;
    private Integer source;
    private Integer price;
    private Integer priceUnit;
    private Integer giftId;
    private Date sendTime;
}
