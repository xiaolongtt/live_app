package org.example.living.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:29
 * @注释
 */
@Data
public class LivingRoomRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3253408187048617285L;

    private Integer id;
    private Long anchorId;
    private String roomName;
    private String covertImg;
    private Integer type;
    private Integer watchNum;
    private Integer goodNum;
    private Long pkObjId;
}
