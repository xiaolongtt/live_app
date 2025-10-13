package org.example.live.user.api.controller;

import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.example.live.user.api.Service.ImServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 16:28
 * @注释
 */
@RequestMapping("/im")
@RestController
public class ImController {

    @Resource
    private ImServer imServer;

    /**
     * 返回给前端相关数据，token和ip地址
     * @return
     */
    @PostMapping("/getConfig")
    public WebResponseVO getConfig(){
        return WebResponseVO.success(imServer.getConfig());
    }

}
