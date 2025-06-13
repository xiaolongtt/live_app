package org.example.live.user.provider.Service;

import org.example.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/1/25 13:59
 * @注释
 */
public interface IUserService {
    /**
     * 根据用户id获取用户信息
     * @param userid
     * @return
     */
    UserDTO GetUserById(Long userid);

    /**
     * 更新用户信息
     * @param userDTO
     * @return
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 添加用户信息
     * @param userDTO
     * @return
     */
    boolean insertUser(UserDTO userDTO);

    /**
     * 批量获取用户信息
     * @param userIds
     * @return
     */
    Map<Long,UserDTO> GetUserByIds(List<Long> userIds);
}
