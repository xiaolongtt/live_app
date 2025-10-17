package com.example.livegiftinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author idea
 * @Date: Created in 14:58 2023/7/30
 * @Description
 */
@Data
public class GiftConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2285354775828848375L;

    private Integer giftId;
    private Integer price;
    private String giftName;
    private Integer status;
    private String coverImgUrl;
    private String svgaUrl;
    private Date createTime;
    private Date updateTime;

}
