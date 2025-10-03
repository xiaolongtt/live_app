package org.example.live.gateway.filter;

import com.example.liveappaccountinterface.IAccountTokenRPC;
import jakarta.annotation.Resource;
import org.example.live.common.interfaces.Enum.GatewayHeaderEnum;
import org.example.live.gateway.properties.GatewayApplicationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 17:16
 * @注释 自定义过滤器类
 */
public class AccountCheckFilter implements GlobalFilter, Ordered {


    @Resource
    private GatewayApplicationProperties gatewayApplicationProperties;

    @Resource
    private IAccountTokenRPC accountTokenRPC;

    /**
     * 主要对请求的url进行判断，判断是否需要进行账号校验
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求的url,判断url是否在白名单中存在，如果存在就直接放行
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();//获取到请求的地址
        if(StringUtils.isEmpty(path)){
            return Mono.empty();
        }
        //开始判断是否在白名单中
        List<String> notCheckUrlList = gatewayApplicationProperties.getNotCheckUrlList();
        for (String notCheckUrl : notCheckUrlList) {
            if(path.startsWith(notCheckUrl)){
                //在白名单中，直接放行
                return chain.filter(exchange);
            }
        }
        //如果不在白名单中，那么要对其cookie其中的token进行校验
        List<HttpCookie> tokenList = request.getCookies().get("live_token");
        if(tokenList==null || tokenList.isEmpty()){
            //如果cookie中没有token，那么就直接返回错误信息
            return Mono.empty();
        }
        String token = tokenList.get(0).getValue();
        if(token.isEmpty()){
            return Mono.empty();
        }
        //现在要去验证token是否有效
        Long userId = accountTokenRPC.getUserIdByToken(token);
        if(userId==null){
            return Mono.empty();
        }
        //获取到用户id后，主要是要把用户id传送给后面的业务层使用
        // gateway --(header)--> springboot-web(interceptor-->get header)
        ServerHttpRequest.Builder builder = request.mutate();
        builder.header(GatewayHeaderEnum.USER_LOGIN_ID.getName(), String.valueOf(userId));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    //这个方法主要用来设置过滤器的优先级
    @Override
    public int getOrder() {
        return 0;
    }
}
