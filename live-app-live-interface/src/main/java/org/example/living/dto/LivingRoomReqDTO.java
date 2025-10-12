package org.example.living.dto;

import jdk.jfr.DataAmount;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:28
 * @注释
 */
@Data
public class LivingRoomReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 798431159296042616L;
    private Integer id;
    private Long anchorId;
    private Long pkObjId;
    private String roomName;
    private Integer roomId;
    private String covertImg;
    private Integer type;
    private Integer appId;
    private int page;
    private int pageSize;
}
