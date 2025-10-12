package com.example.liveappiminterface.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 13:31
 * @注释
 */
@Data
public class ImMsgBodyDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -1197285566966086252L;
    /**
     * 唯一的消息id标识，主要用来消息确认
     */
    private String msgId;
    /**
     * 接入im服务的各个业务线id
     */
    private int appId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 从业务服务中获取，用于在im服务建立连接时使用，从中获取userId与userId进行比较
     */
    private String token;
    /**
     * 业务类型标识，主要确定传输的数据的作用
     */
    private int bizCode;
    /**
     * 和业务服务进行消息传递
     */
    private String data;
}
