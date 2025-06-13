package org.example.live.user.provider.dao.po;

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
 * @Date 2025/2/8 22:07
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_phone")
public class UserPhonePO {
    @TableId(type = IdType.INPUT)
    private String phone;
    private Long userId;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
