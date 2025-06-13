package org.example.live.msg.provicer.Dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 17:57
 * @注释 消息po类
 */
@TableName("t_sms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer code;
    private String phone;
    private Date sendTime;
    private Date updateTime;
}