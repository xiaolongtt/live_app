package com.example.liveappimcoreserver.handler.Tcp;

import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappimcoreserver.common.ImMsg;
import com.example.liveappimcoreserver.handler.Impl.ImHandlerFactoryImpl;
import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 10:09
 * @注释 IM消息核心处理类，主要是根据消息的code字段进行路由到不同的处理方法
 */
@Component
public class TcpImCoreServerHandler extends SimpleChannelInboundHandler {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private ImHandlerFactoryImpl imHandlerFactory;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(!(o instanceof ImMsg)){
            throw new IllegalArgumentException("o is not ImMsg");
        }
        ImMsg imMsg = (ImMsg) o;
        imHandlerFactory.createHandler(channelHandlerContext,imMsg);
    }

    /**
     * 无论是正常下线还是掉线都会回到这个方法，可以用来进行通道的关闭和资源的释放
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if(userId!=null && appId!=null){
            //将用户与服务器ip绑定关系删除
            redisTemplate.delete(ImCoreServerConstants.IM_BIND_IP_KEY + appId + userId);
            //将用户与通道的关系删除
            ChannelHandlerContextCache.remove(userId);
        }
    }
}
