package org.example.live.user.provider.Rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.user.dto.UserDTO;
import org.example.live.user.interfaces.IUserRpc;
import org.example.live.user.provider.Service.IUserService;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/1/16 18:40
 * @注释 Dubbo服务的实现类
 */
//DubboService用来暴露服务，提供给别人访问
@DubboService
public class UserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;
    @Override
    public UserDTO GetUserById(Long userid) {
        return userService.GetUserById(userid);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    @Override
    public boolean insertUser(UserDTO userDTO) {
        return userService.insertUser(userDTO);
    }

    public Map<Long,UserDTO>  GetUserByIds(List<Long> ids){
        return userService.GetUserByIds(ids);
    }

}
