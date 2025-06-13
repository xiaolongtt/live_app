package org.example.live.user.provider.Service;

import org.example.live.user.dto.UserLoginDTO;
import org.example.live.user.dto.UserPhoneDTO;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 21:39
 * @注释
 */
public interface IUserPhoneService {
    /**
     * 手机号登录
     * @param phone
     * @return
     */
    UserLoginDTO login(String phone);
    /**
     * 更具用户 id 查询手机信息
     * @param userId
     * @return
     */
    List<UserPhoneDTO> queryByUserId(Long userId);
    /**
     * 根据手机号查询手机信息
     * @param phone
     * @return
     */
    UserPhoneDTO queryByPhone(String phone);
}
