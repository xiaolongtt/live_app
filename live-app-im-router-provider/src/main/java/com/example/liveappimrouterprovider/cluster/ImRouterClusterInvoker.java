package com.example.liveappimrouterprovider.cluster;

import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;

import java.util.List;
import java.util.stream.Stream;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/8 11:06
 * @注释 在dubbo进行节点选择时，这个类中可以自定义选择的规则
 */
public class ImRouterClusterInvoker<T> extends AbstractClusterInvoker<T> {
    public ImRouterClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    /**
     * 在进行Rpc调用时会经过这个方法，在其中找到对应的ip地址，根据ip地址进行节点选择
     * @param invocation
     * @param list
     * @param loadbalance
     * @return
     * @throws RpcException
     */
    @Override
    protected Result doInvoke(Invocation invocation, List list, LoadBalance loadbalance) throws RpcException {
        checkWhetherDestroyed();
        String ip = (String) RpcContext.getContext().get("IP");
        if(ip==null||ip.isEmpty()){
            throw new RpcException("IP地址不能为空");
        }
        //根据ip地址进行节点选择
        //先获取到全部的节点
        List<Invoker<T>> invokers = list(invocation);
        //找到对应的服务器
        Invoker<T> matchInvoker = invokers.stream().filter(invoker -> {
            //判断节点的ip地址是否与请求中的ip地址一致
            String invokerIp = invoker.getUrl().getHost() + ":" + invoker.getUrl().getPort();
            return invokerIp.equals(ip);
        }).findFirst().orElse(null);
        if(matchInvoker==null){
            throw new RpcException("没有找到对应的服务器");
        }
        return matchInvoker.invoke(invocation);
    }
}
