package org.example.live.user.api.Vo.Resp;

import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 16:30
 * @注释 IM配置VO
 */
@Data
public class ImConfigVo {
    private String token;
    private String wsImServerAddress;
    private String tcpImServerAddress;
}
