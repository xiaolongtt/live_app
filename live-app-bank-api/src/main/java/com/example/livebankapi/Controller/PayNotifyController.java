package com.example.livebankapi.Controller;

import com.example.livebankapi.Service.IPayNotifyService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 13:47
 * @注释 处理支付回调方法
 */
@RestController
@RequestMapping("/payNotify")
public class PayNotifyController {
    @Resource
    private IPayNotifyService payNotifyService;

    @PostMapping("/wxNotify")
    public String wxNotify(@RequestParam("param") String param) {
        return payNotifyService.notifyHandler(param);
    }

}
