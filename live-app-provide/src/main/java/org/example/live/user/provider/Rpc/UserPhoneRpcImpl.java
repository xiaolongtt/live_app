package org.example.live.user.provider.Rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.user.dto.UserLoginDTO;
import org.example.live.user.dto.UserPhoneDTO;
import org.example.live.user.interfaces.IUserPhoneRpc;
import org.example.live.user.provider.Service.IUserPhoneService;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 21:38
 * @注释
 */
@DubboService
public class UserPhoneRpcImpl implements IUserPhoneRpc {
    @Resource
    private IUserPhoneService userPhoneRpc;
    @Override
    public UserLoginDTO login(String phone) {
        return userPhoneRpc.login(phone);
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        return userPhoneRpc.queryByUserId(userId);
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        return userPhoneRpc.queryByPhone(phone);
    }
}
