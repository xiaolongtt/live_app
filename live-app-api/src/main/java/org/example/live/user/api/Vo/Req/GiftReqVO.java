package org.example.live.user.api.Vo.Req;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 11:28
 * @注释
 */
@Data
public class GiftReqVO {
    private int giftId;
    private Integer roomId;
    private Long senderUserId;
    private Long receiverId;
    private int type;
}
