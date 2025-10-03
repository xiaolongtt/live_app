package org.example.live.user.api.controller;

import com.example.webstarter.Interceptor.LiveRequestContext;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 20:50
 * @注释
 */
@RestController
@RequestMapping("/home")
public class HomeController {
    /**
     * @version 1.0
     * @Author xiaolong
     * @Date 2025/10/3 20:50
     * @注释 初始化首页
     */
    @RequestMapping("/initPage")
    public WebResponseVO initPage(){
        //要先从线程中获取到用户id
        Long userID = LiveRequestContext.getUserID();
        if(userID==null){
            return WebResponseVO.errorParam("用户id不能为空");
        }
        return null;
    }

}
