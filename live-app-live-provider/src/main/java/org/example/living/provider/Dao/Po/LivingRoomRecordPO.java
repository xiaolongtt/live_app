package org.example.living.provider.Dao.Po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:14
 * @注释 直播记录表
 */
@TableName("t_living_room_record")
@Data
public class LivingRoomRecordPO {
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
    private Date endTime;
    private Date updateTime;
}
