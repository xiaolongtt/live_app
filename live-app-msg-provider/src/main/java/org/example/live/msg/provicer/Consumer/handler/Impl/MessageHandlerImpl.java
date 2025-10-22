package org.example.live.msg.provicer.Consumer.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserverinterface.constants.ImMsgBizCodeEnum;
import com.example.liveappiminterface.constants.AppIdEnum;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import com.example.liveappimrouterinterface.Rpc.ImRouterRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.live.msg.dto.MessageDTO;
import org.example.live.msg.provicer.Consumer.handler.MessageHandler;
import org.example.living.dto.LivingRoomReqDTO;
import org.example.living.interfaces.ILivingRoomRpc;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 19:27
 * @注释
 */
@Component
public class MessageHandlerImpl implements MessageHandler {

    @DubboReference
    private ImRouterRpc imRouterRpc;

    @DubboReference
    private ILivingRoomRpc iLivingRoomRpc;

    @Override
    public void handleMsg(ImMsgBodyDto imMsgBodyDto) {
        int bizCode = imMsgBodyDto.getBizCode();
        if(ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode()==bizCode){
            // 直播业务线 一个人发送 n个人接收
            // 根据roomId，appId 去调用rpc方法，获取对应的直播间内的userId
            //获取发送的消息
            MessageDTO messageDTO = JSON.parseObject(imMsgBodyDto.getData(), MessageDTO.class);
            Integer roomId = messageDTO.getRoomId();
            LivingRoomReqDTO livingRoomReqDTO=new LivingRoomReqDTO();
            livingRoomReqDTO.setRoomId(roomId);
            livingRoomReqDTO.setAppId(imMsgBodyDto.getAppId());
            //获取到这次要群发的用户id
            List<Long> userIds = iLivingRoomRpc.queryUserIdsByRoomId(livingRoomReqDTO);
            List<ImMsgBodyDto> imMsgBodies = new ArrayList<>();
            //设置群发的消息主题都一样
            userIds.forEach(userId ->{
                ImMsgBodyDto respMsg = new ImMsgBodyDto();
                respMsg.setUserId(userId);
                respMsg.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                respMsg.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode());
                respMsg.setData(JSON.toJSONString(messageDTO));
                imMsgBodies.add(respMsg);
            } );
            imRouterRpc.batchSendMsg(imMsgBodies);
        }
    }
}
