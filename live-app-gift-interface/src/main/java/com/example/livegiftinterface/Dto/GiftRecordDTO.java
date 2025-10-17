package com.example.livegiftinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 19:53
 * @注释
 */
@Data
public class GiftRecordDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1683399360102461356L;
    private Long id;
    private Long userId;
    private Long objectId;
    private Integer source;
    private Integer price;
    private Integer priceUnit;
    private Integer giftId;
    private Date sendTime;
}
