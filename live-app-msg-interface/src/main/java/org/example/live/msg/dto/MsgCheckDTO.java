package org.example.live.msg.dto;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 17:58
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgCheckDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3394248744287019717L;
    /**
     * 验证结果
     */
    private boolean checkStatus;
    /**
     * 验证结果描述
     */
    private String desc;
}
