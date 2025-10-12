package org.example.live.user.api.Vo.Resp;

import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 19:58
 * @注释 返回给前端的直播间列表类
 */
@Data
public class LivingRoomPageRespVO {
    private List<LivingRoomRespVO> list;
    private boolean hasNext;
}
