package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappiminterface.Rpc.ImTokenRpc;
import com.example.liveappiminterface.constants.AppIdEnum;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.constants.ImConstants;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        int appId = imMsgBodyDto.getAppId();
        //进行参数校验
        if(token==null||token.isEmpty() || userId<=1000 || appId<=10000){
            ctx.close();
            throw new IllegalArgumentException("token is null or empty");
        }
        Long userIdByToken = imTokenRpc.getUserIdByToken(token);
        //先校验token是否正确，然后给客户端返回登录成功的消息
        if(userIdByToken!=null && userIdByToken.equals(imMsgBodyDto.getUserId())){
            loginSuccessHandler(ctx,userId,appId);
            return ;
        }
        ctx.close();
        throw new IllegalArgumentException("token is not valid");
    }

    /**
     * 将登录成功单独抽离出来，供外界使用
     * @param ctx
     * @param userId
     * @param appId
     */
    public void loginSuccessHandler(ChannelHandlerContext ctx,Long userId,int appId){
        //将channel与userId关联起来
        ChannelHandlerContextCache.put(userId,ctx);
        //给channel绑定上对应的id
        ImContextUtils.setUserId(userId,ctx);
        ImContextUtils.setAppId(appId,ctx);
        //将信息返回给客户端
        ImMsgBodyDto imLoginSuccessBodyDto=new ImMsgBodyDto();
        imLoginSuccessBodyDto.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        imLoginSuccessBodyDto.setUserId(userId);
        imLoginSuccessBodyDto.setData("success");
        ImMsg RespimMsg = ImMsg.build(ImCodeEnum.IM_LOGIN.getCode(), JSON.toJSONString(imLoginSuccessBodyDto));
        //用户登录成功后将用户id与对应的服务器ip地址缓存下来，过期时间为心跳时间的两倍，在每次收到心跳包以后要更新时间，userid的生成使用一个定量类的属性
        stringRedisTemplate.opsForValue().set(ImCoreServerConstants.IM_BIND_IP_KEY+userId+appId,
                ChannelHandlerContextCache.getServerIpAddress(),
                ImConstants.DEFAULT_HEART_BEAT_TIME*2, TimeUnit.SECONDS);
        ctx.writeAndFlush(RespimMsg);
    }

}
