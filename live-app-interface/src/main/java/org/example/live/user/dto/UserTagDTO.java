package org.example.live.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/7 16:15
 * @注释
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserTagDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -15151515151L;
    private Long userId;
    private Long tagInfo01;
    private Long tagInfo02;
    private Long tagInfo03;
    private Date createTime;
    private Date updateTime;
}
