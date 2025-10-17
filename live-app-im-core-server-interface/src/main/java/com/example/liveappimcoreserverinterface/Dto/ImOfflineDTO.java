package com.example.liveappimcoreserverinterface.Dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 23:18
 * @注释
 */
@Data
public class ImOfflineDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3903207655076898293L;
    private Long userId;
    private Integer appId;
    private Integer roomId;
    private Long loginTime;
}
