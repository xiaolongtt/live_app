package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappiminterface.Rpc.ImTokenRpc;
import com.example.liveappiminterface.constants.AppIdEnum;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 11:24
 * @注释 登录消息包：登录token验证，channel 和 userId 关联
 */
@Component
public class ImLoginMsgHandlerImpl implements SimpleHandler {

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        byte[] body = imMsg.getBody();
        if(body==null||body.length==0){
            ctx.close();
            throw new IllegalArgumentException("body is null or empty");
        }
        //要将body转换未ImMsgBodyDto
        ImMsgBodyDto imMsgBodyDto = JSON.parseObject(new String(body), ImMsgBodyDto.class);
        //要先获取到Token。确保是登录消息
        String token = imMsgBodyDto.getToken();
        Long userId = imMsgBodyDto.getUserId();
        Integer appId = imMsgBodyDto.getAppId();
        //进行参数校验
        if(token==null||token.isEmpty() || userId<=1000 || appId<=10000){
            ctx.close();
            throw new IllegalArgumentException("token is null or empty");
        }
        Long userIdByToken = imTokenRpc.getUserIdByToken(token);
        //先校验token是否正确，然后给客户端返回登录成功的消息
        if(userIdByToken!=null && userIdByToken.equals(imMsgBodyDto.getUserId())){
            //将channel与userId关联起来
            ChannelHandlerContextCache.put(userIdByToken,ctx);
            //给channel绑定上对应的id
            ImContextUtils.setUserId(userIdByToken,ctx);
            //将信息返回给客户端
            ImMsgBodyDto imLoginSuccessBodyDto=new ImMsgBodyDto();
            imLoginSuccessBodyDto.setMsgId(imMsgBodyDto.getMsgId());
            imLoginSuccessBodyDto.setAppId(AppIdEnum.LIVE_BIZ.getCode());
            imLoginSuccessBodyDto.setData("success");
            ImMsg RespimMsg = ImMsg.build(ImCodeEnum.IM_LOGIN.getCode(), JSON.toJSONString(imLoginSuccessBodyDto));
            ctx.writeAndFlush(RespimMsg);
        }
        ctx.close();
        throw new IllegalArgumentException("token is not valid");
    }
}
