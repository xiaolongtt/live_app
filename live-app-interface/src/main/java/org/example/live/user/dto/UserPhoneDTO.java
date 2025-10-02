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
 * @Date 2025/2/8 21:37
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5751791250926532327L;
    private String phone;
    private Long userId;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
