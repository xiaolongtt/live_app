package org.example.live.user.api.Vo.Resp;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 11:33
 * @注释
 */
@Data
public class GiftConfigVO {
    private Integer giftId;
    private Integer price;
    private String giftName;
    private Integer status;
    private String coverImgUrl;
    private String svgaUrl;
    private Date createTime;
    private Date updateTime;
}
