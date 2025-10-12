package org.example.live.user.api.Vo.Req;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 20:41
 * @注释 前端请求直播间列表参数
 */
@Data
public class LivingRoomReqVO {
    private int page;
    private int pageSize;
    private int type;
}
