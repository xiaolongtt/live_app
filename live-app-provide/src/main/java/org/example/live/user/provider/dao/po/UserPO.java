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
 * @Date 2025/1/25 13:54
 * @注释 用户po类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class UserPO {

    @TableId(type = IdType.INPUT)
    private Long userId;
    private String nickName;
    private String trueName;
    private String avatar;
    private Integer sex;
    private Integer workCity;
    private Integer bornCity;
    private Date bornDate;
    private Date createTime;
    private Date updateTime;
}