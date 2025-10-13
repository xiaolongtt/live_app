package org.example.live.user.api.controller;

import com.example.webstarter.Error.BizBaseErrorEnum;
import com.example.webstarter.Error.ErrorAssert;
import com.example.webstarter.Interceptor.LiveRequestContext;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.example.live.user.api.Error.ApiErrorEnum;
import org.example.live.user.api.Service.ILivingRoomService;
import org.example.live.user.api.Vo.Req.LivingRoomReqVO;
import org.example.live.user.api.Vo.Resp.LivingRoomPageRespVO;
import org.example.live.user.api.Vo.Resp.LivingRoomRespVO;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.dto.LivingRoomRespDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/11 23:02
 * @注释 直播间相关接口
 */
@RestController
@RequestMapping("/livingRoom")
public class LivingRoomController {

    @Resource
    private ILivingRoomService livingRoomService;


    /**
     * 展示直播间列表
     * @param livingRoomReqVO
     * @return
     */
    @PostMapping("/list")
    public WebResponseVO listLivingRoom(LivingRoomReqVO livingRoomReqVO){
        ErrorAssert.isNotNull(livingRoomReqVO,BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(livingRoomReqVO.getPage() > 0&&livingRoomReqVO.getPageSize()<=100, BizBaseErrorEnum.PARAM_ERROR);
        return new WebResponseVO().success(livingRoomService.listLivingRoom(livingRoomReqVO));
    }

    /**
     * 开启直播间
     * @param type
     * @return
     */
    @PostMapping("/startingLive")
    public WebResponseVO startingLiving(Integer type) {
        ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        boolean result = livingRoomService.startingLiving(type);
        if(result){
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("开启直播间失败");
    }

    /**
     * 关闭直播间
     * @param roomId
     * @return
     */
    @PostMapping("closingLive")
    public WebResponseVO closingLiving(Integer roomId){
        ErrorAssert.isNotNull(roomId, BizBaseErrorEnum.PARAM_ERROR);
        boolean result = livingRoomService.closingLiving(roomId);
        if(result){
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("关闭直播间失败");
    }

    /**
     * 获取主播相关配置信息（只有主播才会有权限）
     * @param roomId
     * @return
     */
    @PostMapping("/anchorConfig")
    public WebResponseVO anchorConfig(Integer roomId) {
        return WebResponseVO.success(livingRoomService.anchorConfig(LiveRequestContext.getUserID(), roomId));
    }
}
