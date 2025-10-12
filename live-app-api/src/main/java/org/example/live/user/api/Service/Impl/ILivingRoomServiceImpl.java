package org.example.live.user.api.Service.Impl;

import com.example.webstarter.Interceptor.LiveRequestContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.common.interfaces.Utils.ConvertBeanUtils;
import org.example.live.common.interfaces.Utils.PageWrapper;
import org.example.live.user.api.Service.ILivingRoomService;
import org.example.live.user.api.Vo.LivingRoomInitVO;
import org.example.live.user.api.Vo.Req.LivingRoomReqVO;
import org.example.live.user.api.Vo.Resp.LivingRoomPageRespVO;
import org.example.live.user.api.Vo.Resp.LivingRoomRespVO;
import org.example.live.user.dto.UserDTO;
import org.example.live.user.interfaces.IUserRpc;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.dto.LivingRoomRespDTO;
import org.example.living.interfaces.ILivingRoomRpc;
import org.example.living.provider.Dao.Po.LivingRoomPO;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/11 23:31
 * @注释
 */
@Service
public class ILivingRoomServiceImpl implements ILivingRoomService {

    @DubboReference
    private ILivingRoomRpc livingRoomRpc;
    @DubboReference
    private IUserRpc userRpc;
    /**
     * 开启直播间
     * @param type
     * @return
     */
    @Override
    public boolean startingLiving(Integer type) {
        Long userID = LiveRequestContext.getUserID();
        UserDTO userDTO = userRpc.GetUserById(userID);
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAnchorId(userID);
        livingRoomReqDTO.setType(type);
        livingRoomReqDTO.setRoomName("主播-" + LiveRequestContext.getUserID() + "的直播间");
        livingRoomReqDTO.setCovertImg(userDTO.getAvatar());
        return livingRoomRpc.startingLiving(livingRoomReqDTO)>0;

    }
    /**
     * 关闭直播间
     * @param roomId
     * @return
     */
    @Override
    public boolean closingLiving(Integer roomId) {
        Long userID = LiveRequestContext.getUserID();
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setRoomId(roomId);
        livingRoomReqDTO.setAnchorId(userID);
        return livingRoomRpc.closingLiving(livingRoomReqDTO);
    }

    /**
     * 根据房间id去
     * @param userID
     * @param roomId
     * @return
     */
    @Override
    public LivingRoomInitVO anchorConfig(Long userID, Integer roomId) {
        LivingRoomRespDTO livingRoomRespDTO = livingRoomRpc.queryByRoomId(roomId);
        LivingRoomInitVO livingRoomInitVO=new LivingRoomInitVO();
        if(livingRoomRespDTO==null||livingRoomRespDTO.getAnchorId()==null||userID==null){
            livingRoomInitVO.setAnchor(false);
        }else{
            livingRoomInitVO.setAnchor(livingRoomRespDTO.getAnchorId().equals(userID));
        }
        return livingRoomInitVO;
    }

    /**
     * 分页查询直播间列表
     * @param livingRoomReqVO
     * @return
     */
    @Override
    public LivingRoomPageRespVO listLivingRoom(LivingRoomReqVO livingRoomReqVO) {
        PageWrapper<LivingRoomRespDTO> pageWrapper = livingRoomRpc.list(ConvertBeanUtils.convert(livingRoomReqVO, LivingRoomReqDTO.class));
        LivingRoomPageRespVO livingRoomPageRespVO =new LivingRoomPageRespVO();
        livingRoomPageRespVO.setList(ConvertBeanUtils.convertList(pageWrapper.getData(), LivingRoomRespVO.class));
        livingRoomPageRespVO.setHasNext(pageWrapper.isHasNext());
        return livingRoomPageRespVO;
    }
}
