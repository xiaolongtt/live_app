package org.example.live.user.api.Vo.Resp;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 19:59
 * @注释 返回给前端的单个直播间属性类
 */
@Data
public class LivingRoomRespVO {
    private Integer id;
    private String roomName;
    private Long anchorId;
    private Integer watchNum;
    private Integer goodNum;
    private Integer type;
    private String covertImg;
}
