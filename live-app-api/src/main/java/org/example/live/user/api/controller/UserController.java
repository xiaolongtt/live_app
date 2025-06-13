package org.example.live.user.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.live.user.dto.UserDTO;
import org.example.live.user.interfaces.IUserRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/1/17 20:15
 * @注释
 */
@RestController
@RequestMapping("/User")
public class UserController {

//    使用DubboReference进行远程RPC注入
    @DubboReference
    private IUserRpc iUserRpc;
    @GetMapping("/getUserInfo")
    public UserDTO GetUserById(Long userid){
        return iUserRpc.GetUserById(userid);
    }
    @PostMapping("/updateUserInfo")
    public boolean updateUserInfo(UserDTO userDTO){
        return iUserRpc.updateUserInfo(userDTO);
    }
    @PostMapping("/insertUser")
    public boolean insertUser(UserDTO userDTO){
        return iUserRpc.insertUser(userDTO);
    }
    @GetMapping("/getUsersByIds")
    public Map<Long,UserDTO> GetUserByIds(String userIds){
        String[] ids = userIds.split(",");
        List<Long> list = Arrays.stream(ids).map(id -> Long.valueOf(id)).collect(Collectors.toList());
        return iUserRpc.GetUserByIds(list);
    }


}
