package com.example.livebankinterface.Dto;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 20:21
 * @注释
 */
@Data
public class SendGiftMqDto {
    private Long userId;
    private Integer giftId;
    private Integer price;
    private Long receiverId;
    private Integer roomId;
    //用来标识一次礼物发送请求，防止重复发送
    private String uuid;
    private String url;
}
