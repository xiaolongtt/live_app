package org.example.living.provider.Service;

import org.example.live.common.interfaces.Utils.PageWrapper;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.dto.LivingRoomRespDTO;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:19
 * @注释
 */
public interface ILivingRoomService {
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

     /**
      * 从数据库中查找全部直播间列表
      *
      * @param livingRoomReqDTO
      * @return
      */
     List<LivingRoomRespDTO> selectAllFromDb(Integer type);

}
