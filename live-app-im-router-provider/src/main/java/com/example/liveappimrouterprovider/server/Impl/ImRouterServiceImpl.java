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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 给若干用户发送消息
     * 一般来说假如有10太im服务器ip,所以就算有1000个用户来发送，也只有10中可能的ip地址，所以可以先根据ip地址将用户id分类，然后将消息集体发送到对应的服务器
     * @param msgList
     */
    @Override
    public void batchSendMsg(List<ImMsgBodyDto> msgList) {
        if(!msgList.isEmpty()){
            int appId = msgList.get(0).getAppId();
            List<Long> userIds = msgList.stream().map(ImMsgBodyDto::getUserId).toList();
            //使用一个map来存储每个ip地址对应的用户id列表
            Map<String, List<Long>> ipToUserIdsMap = new HashMap<>();
            userIds.forEach(userId->{
                String serverIp = stringRedisTemplate.opsForValue().get(ImCoreServerConstants.IM_BIND_IP_KEY + userId + appId);
                if(serverIp!=null && !serverIp.isEmpty()){
                    if(ipToUserIdsMap.containsKey(serverIp)){
                        ipToUserIdsMap.get(serverIp).add(userId);
                    }else{
                        ipToUserIdsMap.put(serverIp,new ArrayList<>());
                        ipToUserIdsMap.get(serverIp).add(userId);
                    }
                }
            });
            //根据服务器ip将用户id分类存放，现在要将用户id批量发送
            for (String ip : ipToUserIdsMap.keySet()) {
                //获取到一批用户要发送到的ip地址
                RpcContext.getContext().set("IP",ip);
                List<Long> userIdsToSend = ipToUserIdsMap.get(ip);
                //将这批用户id发送到一个服务器中
                List<ImMsgBodyDto> msgToSend = msgList.stream().filter(msg -> userIdsToSend.contains(msg.getUserId())).collect(Collectors.toList());
                imRouterHandlerRpc.batchRouterMsg(msgToSend);
            }
        }


    }
}
