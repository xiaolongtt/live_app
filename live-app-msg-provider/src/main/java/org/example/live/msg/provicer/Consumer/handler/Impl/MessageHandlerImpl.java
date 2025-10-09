package org.example.live.msg.provicer.Consumer.handler.Impl;

import com.example.liveappiminterface.dto.ImMsgBodyDto;
import org.example.live.msg.constants.ImMsgBizCodeEnum;
import org.example.live.msg.provicer.Consumer.handler.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 19:27
 * @注释
 */
@Component
public class MessageHandlerImpl implements MessageHandler {
    @Override
    public void handleMsg(ImMsgBodyDto imMsgBodyDto) {
        int bizCode = imMsgBodyDto.getBizCode();
        if(ImMsgBizCodeEnum.LIVESTREAM.getCode()==bizCode){
            // 直播业务线
            //TODO
            System.out.println("直播业务线");
        }
    }
}
