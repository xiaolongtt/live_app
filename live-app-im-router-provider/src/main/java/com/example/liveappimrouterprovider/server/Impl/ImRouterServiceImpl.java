package com.example.liveappimrouterprovider.server.Impl;

import com.example.liveappimcoreserverinterface.constants.ImCoreServerConstants;
import com.example.liveappimcoreserverinterface.rpc.ImRouterHandlerRpc;
import com.example.liveappiminterface.dto.ImMsgBodyDto;
import com.example.liveappimrouterprovider.server.ImRouterService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 16:59
 * @注释 根据用户发来的服务器ip地址，让指定的服务器与用户建立链接
 * 请求路径：
 * 客户端请求 → Dubbo消费者 → RpcContext设置IP → ImRouterCluster →
 * ImRouterClusterInvoker → 根据IP路由 → 目标IM服务器
 */
@Service
public class ImRouterServiceImpl implements ImRouterService {

    @DubboReference
    private ImRouterHandlerRpc imRouterHandlerRpc;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendMsg(ImMsgBodyDto msg) {
        Long userId = msg.getUserId();
        int appId = msg.getAppId();
        if(userId<=1000 || appId<=10000){
            throw new IllegalArgumentException("userId or appId is invalid");
        }
        //先获取到用户绑定的服务器ip地址
        String serverIp = stringRedisTemplate.opsForValue().get(ImCoreServerConstants.IM_BIND_IP_KEY + userId + appId);
        if(serverIp==null||serverIp.isEmpty()){
            throw new IllegalArgumentException("serverIp is null or empty");
        }
        //在RPC上下文中设置IP地址，当进行rpc调用时，会将这个IP地址设置到请求头中
        RpcContext.getContext().set("IP",serverIp);
        imRouterHandlerRpc.routerMsg(msg);
    }
}
