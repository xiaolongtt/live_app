package org.example.live.user.api.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.example.live.user.api.Service.IUserLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 9:53
 * @注释
 */
@RestController
@RequestMapping("/userLogin")
public class UserLoginController {
    @Resource
    private IUserLoginService userLoginService;

    // 发送验证码
    @PostMapping("/sendLoginCode")
    public WebResponseVO sendLoginCode(String phone) {
        return userLoginService.sendLoginCode(phone);
    }

    // 登录请求 验证码是否合法 -> 初始化注册/老用户登录
    @PostMapping("/login")
    public WebResponseVO login(String phone, Integer code, HttpServletResponse response) {
        return userLoginService.login(phone, code, response);
    }
}
