package org.example.live.user.api.Vo;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 16:38
 * @注释
 */
@Data
public class LivingRoomInitVO {
    private Long anchorId;
    private Long userId;
    private String anchorImg;
    private String roomName;
    /**
     * 是否为主播，通过这个字段判断用户有没有开关播权力
     */
    private boolean isAnchor;
    private String avatar;
    private Integer roomId;
    private String watcherNickName;
    private String anchorNickName;
    //观众头像
    private String watcherAvatar;
    //默认背景图，为了方便讲解使用
    private String defaultBgImg;
    private Long pkObjId;
}
