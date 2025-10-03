package org.example.live.user.api.Service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 9:56
 * @注释
 */
@Service
public interface IUserLoginService {
    /**
     * 发送登录验证码
     * @param phone
     * @return
     */
    WebResponseVO sendLoginCode(String phone);

    /**
     * 登录请求 验证码是否合法 -> 初始化注册/老用户登录
     * @param phone
     * @param code
     * @param response
     * @return
     */
    WebResponseVO login(String phone, Integer code, HttpServletResponse response);
}
