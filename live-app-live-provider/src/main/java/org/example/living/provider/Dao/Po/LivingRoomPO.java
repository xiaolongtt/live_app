package org.example.living.provider.Dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author xiaolong
 * @Date 2025/10/12 12:14
 * @version 1.0
 * @注释 正在直播的直播间表
 */
@TableName("t_living_room")
@Data
public class LivingRoomPO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long anchorId;
    private Integer type;
    private String roomName;
    private String covertImg;
    private Integer status;
    private Integer watchNum;
    private Integer goodNum;
    private Date startTime;
    private Date updateTime;
}
