package com.example.liveappimcoreserverinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 23:16
 * @注释 主要用来在mq发送数据时封装的数据，主要是用户id和直播间id
 */

@Data
public class ImOnlineDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6878803851812918274L;
    private Long userId;
    private Integer roomId;
    private Integer appId;
    private Long loginTime;
}
