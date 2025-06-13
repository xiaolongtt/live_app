package org.example.live.user.provider.Service.Impl;

import org.example.live.user.dto.UserLoginDTO;
import org.example.live.user.dto.UserPhoneDTO;
import org.example.live.user.provider.Service.IUserPhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 21:40
 * @注释
 */
@Service
public class IUserPhoneServiceImpl implements IUserPhoneService {
    @Override
    public UserLoginDTO login(String phone) {
        return null;
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        return null;
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        return null;
    }
}
