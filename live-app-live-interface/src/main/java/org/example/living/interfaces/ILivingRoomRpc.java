package org.example.living.interfaces;

import org.example.live.common.interfaces.Utils.PageWrapper;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.dto.LivingRoomRespDTO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:22
 * @注释
 */
public interface ILivingRoomRpc {
    /**
     * 开启直播间，向数据库中插入一条开播记录
     * @param livingRoomReqDTO
     * @return
     */
    Integer startingLiving(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 关闭直播间，向数据库中删除一条开播记录
     * @param livingRoomReqDTO
     * @return
     */
    Boolean closingLiving(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 根据直播间id查询直播间信息
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * 分页查询直播间列表
     * @param livingRoomReqDTO
     * @return
     */
    PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);
}
