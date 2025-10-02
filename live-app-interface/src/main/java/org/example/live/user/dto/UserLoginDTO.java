package org.example.live.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 21:36
 * @注释 返回给前端的登录信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1713335694749213410L;

    private Long userId;
    private String token;
    private String desc;
    private Boolean isLoginSuccess;


    public static UserLoginDTO loginSuccess(Long userId, String token) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserId(userId);
        userLoginDTO.setToken(token);
        userLoginDTO.setIsLoginSuccess(true);
        return userLoginDTO;
    }
    public static UserLoginDTO loginFail(String desc) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setDesc(desc);
        userLoginDTO.setIsLoginSuccess(false);
        return userLoginDTO;
    }
}
