package org.example.living.provider.Service;

import com.example.liveappimcoreserverinterface.Dto.ImOfflineDTO;
import com.example.liveappimcoreserverinterface.Dto.ImOnlineDTO;
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
      * 主要用来在用户离开直播间后，将用户id从与roomId相关联的redis集合中移除
      * @param imOnlineDTO
      */
     void userLeaveLivingRoom(ImOfflineDTO imOfflineDTO);

     /**
      * 主要用来在用户进入直播间后，将用户id放入到与roomId相关联的redis集合中
      * @param imOnlineDTO
      */
     void userEnterLivingRoom(ImOnlineDTO imOnlineDTO);
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

     /**
      * 根据直播间id查询直播间中的用户id列表
      * @param livingRoomReqDTO
      * @return
      */
     List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO);

}
