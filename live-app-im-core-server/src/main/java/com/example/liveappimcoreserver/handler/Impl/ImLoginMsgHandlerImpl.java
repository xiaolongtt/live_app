package com.example.liveappimcoreserver.handler.Impl;

import com.alibaba.fastjson.JSON;
import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.SimpleHandler;
import com.example.liveappimcoreserverinterface.Dto.ImOnlineDTO;
import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappiminterface.Rpc.ImTokenRpc;
import com.example.liveappiminterface.constants.AppIdEnum;
import com.example.liveappiminterface.constants.ImCodeEnum;
import com.example.liveappiminterface.constants.ImConstants;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.example.live.common.interfaces.Topic.ImCoreServerProviderTopicNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ImLoginMsgHandlerImpl.class);

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MQProducer mqProducer;

    /**
     * 这里是普通登录的消息处理方法，
     * @param ctx 通道上下文
     * @param imMsg 消息体
     */
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
            loginSuccessHandler(ctx,userId,appId,null);
            return ;
        }
        ctx.close();
        throw new IllegalArgumentException("token is not valid");
    }

    /**
     * 当用户登录成功后，使用消息队列将用户信息发出，在消费端将用户id与直播间id关联起来
     * @param userId
     * @param appId
     * @param roomId
     */
    private void sendMqMsg(Long userId,Integer appId,Integer roomId){
        ImOnlineDTO imOnlineDTO=new ImOnlineDTO();
        imOnlineDTO.setUserId(userId);
        imOnlineDTO.setAppId(appId);
        imOnlineDTO.setLoginTime(System.currentTimeMillis());
        imOnlineDTO.setRoomId(roomId);
        Message message=new Message();
        message.setTopic(ImCoreServerProviderTopicNames.IM_ONLINE_TOPIC);
        message.setBody(JSON.toJSONString(imOnlineDTO).getBytes());
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[sendLoginMQ] sendResult is {}", sendResult);
        } catch (Exception e) {
            LOGGER.error("[sendLoginMQ] error is: ", e);
        }
    }


    /**
     * 将登录成功单独抽离出来，供外界使用，主要登录分为两种，一种是登录系统，一种是登录直播间
     * @param ctx 通道上下文
     * @param userId 用户id
     * @param appId 应用id
     * @param roomId 房间id
     */
    public void loginSuccessHandler(ChannelHandlerContext ctx,Long userId,int appId,Integer roomId){
        //将channel与userId关联起来
        ChannelHandlerContextCache.put(userId,ctx);
        //给channel绑定上对应的id
        ImContextUtils.setUserId(userId,ctx);
        ImContextUtils.setAppId(appId,ctx);
        if (roomId != null) {
            ImContextUtils.setRoomId(ctx, roomId);
        }
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
        sendMqMsg(userId,appId,roomId);
    }

}
