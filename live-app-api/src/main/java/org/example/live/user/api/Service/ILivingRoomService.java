package org.example.live.user.api.Service;

import org.example.live.user.api.Vo.LivingRoomInitVO;
import org.example.live.user.api.Vo.Req.LivingRoomReqVO;
import org.example.live.user.api.Vo.Resp.LivingRoomPageRespVO;
import org.example.living.dto.LivingRoomReqDTO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/11 23:31
 * @注释
 */
public interface ILivingRoomService {
    boolean startingLiving(Integer type);

    boolean closingLiving(Integer roomId);

    LivingRoomInitVO anchorConfig(Long userID, Integer roomId);

    LivingRoomPageRespVO listLivingRoom(LivingRoomReqVO livingRoomReqVO);
}
