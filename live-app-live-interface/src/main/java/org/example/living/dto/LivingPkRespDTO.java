package org.example.living.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:29
 * @注释
 */
@Data
public class LivingPkRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4339054569347876187L;

    private boolean onlineStatus;
    private String msg;
}
