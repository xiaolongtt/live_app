package org.example.living.provider.Rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.common.interfaces.Utils.PageWrapper;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.dto.LivingRoomRespDTO;
import org.example.living.interfaces.ILivingRoomRpc;
import org.example.living.provider.Service.ILivingRoomService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:23
 * @注释
 */
@DubboService
public class livingRoomRpcImpl implements ILivingRoomRpc {

    @Resource
    private ILivingRoomService livingRoomService;
    @Override
    public Integer startingLiving(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.startingLiving(livingRoomReqDTO);
    }


    @Override
    public Boolean closingLiving(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.closingLiving(livingRoomReqDTO);
    }

    @Override
    public LivingRoomRespDTO queryByRoomId(Integer roomId) {
        return livingRoomService.queryByRoomId(roomId);
    }

    @Override
    public PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.list(livingRoomReqDTO);
    }
}
