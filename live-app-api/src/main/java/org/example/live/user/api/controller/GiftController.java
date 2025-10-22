package org.example.live.user.api.controller;

import com.example.livegiftinterface.Dto.GiftConfigDTO;
import com.example.livegiftinterface.Interface.IGiftConfigRpc;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.example.live.user.api.Service.IGiftService;
import org.example.live.user.api.Vo.Req.GiftReqVO;
import org.example.live.user.api.Vo.Resp.GiftConfigVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 11:23
 * @注释
 */
@RestController
@RequestMapping("/gift")
public class GiftController {

    @Resource
    private IGiftService giftService;
    /**
     * 返回礼物列表
     * @return
     */
    @PostMapping("/list")
    public WebResponseVO listGift(){
        List<GiftConfigVO> giftConfigVOS = giftService.listGift();
        if(giftConfigVOS == null){
            return WebResponseVO.bizError("查询礼物列表失败");
        }
        return WebResponseVO.success(giftConfigVOS);
    }

    @PostMapping("/send")
    public WebResponseVO sendGift(GiftReqVO giftReqVO){
        boolean b = giftService.sendGift(giftReqVO);
        if(!b){
            return WebResponseVO.bizError("送礼失败");
        }
        return WebResponseVO.success();
    }
}
