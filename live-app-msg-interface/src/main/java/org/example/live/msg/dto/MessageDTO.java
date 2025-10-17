package org.example.live.msg.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 14:40
 * @注释 发送的消息DTO
 */
@Data
public class MessageDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1394725007809304385L;
    //用户id
    private Long userId;
    //房间id
    private Integer roomId;
    //发送人名称
    private String senderName;
    //发送人头像
    private String senderAvtar;
    /**
     * 消息类型
     */
    private Integer type;
    /**
     * 消息内容
     */
    private String content;
    private Date createTime;
    private Date updateTime;
}
