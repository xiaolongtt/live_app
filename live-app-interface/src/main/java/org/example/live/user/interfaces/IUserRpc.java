package org.example.live.user.interfaces;

import org.example.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/1/16 18:38
 * @注释 dubbo服务暴露出给外部使用的接口，实现类在provider中
 */
public interface IUserRpc {
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

    Map<Long, UserDTO> GetUserByIds(List<Long> list);
}
