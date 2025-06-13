package org.example.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/6 20:13
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_tag")
public class UserTagPO {
    @TableId(type = IdType.INPUT)
    private Long userId;
    @TableField(value = "tag_info_01")
    private Long tagInfo01;
    @TableField(value = "tag_info_02")
    private Long tagInfo02;
    @TableField(value = "tag_info_03")
    private Long tagInfo03;
    private Date createTime;
    private Date updateTime;
}
