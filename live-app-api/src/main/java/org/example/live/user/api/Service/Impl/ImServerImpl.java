package org.example.live.user.api.Service.Impl;

import com.example.liveappimcoreserver.common.ImContextUtils;
import com.example.liveappiminterface.Rpc.ImTokenRpc;
import com.example.liveappiminterface.constants.AppIdEnum;
import com.example.webstarter.Interceptor.LiveRequestContext;
import com.example.webstarter.Interceptor.RequestConstants;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.live.user.api.Service.ImServer;
import org.example.live.user.api.Vo.Resp.ImConfigVo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/13 16:32
 * @注释
 */
@Service
public class ImServerImpl implements ImServer {
    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public ImConfigVo getConfig() {
        ImConfigVo imConfigVo=new ImConfigVo();
        imConfigVo.setToken(imTokenRpc.createTmLoginToken(LiveRequestContext.getUserID(), AppIdEnum.LIVE_BIZ.getCode()));
        //服务器ip的设置可以在
        BuildServerIp(imConfigVo);
        return imConfigVo;
    }
    // 这里是通过DiscoveryClient获取Nacos中的注册信息，实现负载均衡
    private void BuildServerIp(ImConfigVo imConfigVo){
        //在注册中心中获取im-core-server的实例列表
        List<ServiceInstance> instances = discoveryClient.getInstances("qiyu-live-im-core-server");
        //进行打乱实现简单的负载均衡
        Collections.shuffle(instances);
        ServiceInstance serviceInstance = instances.get(0);
        imConfigVo.setWsImServerAddress(serviceInstance.getHost() + ":8086");
        imConfigVo.setTcpImServerAddress(serviceInstance.getHost() + ":8085");
    }

}
