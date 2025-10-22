package com.example.webstarter.Interceptor;

import com.example.webstarter.Error.QiyuErrorException;
import com.example.webstarter.Limit.RequestLimit;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/19 16:58
 * @注释 对于重复请求，要有专门的拦截器去处理
 */
public class RequestLimitInterceptor implements HandlerInterceptor {

    /**
     * 用来记录请求次数的key
     */
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 所有加了注解的方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean IsRequestLimit = handlerMethod.getMethod().isAnnotationPresent(RequestLimit.class);
        if(IsRequestLimit){
            RequestLimit requestLimit = handlerMethod.getMethod().getAnnotation(RequestLimit.class);
            Long userID = LiveRequestContext.getUserID();
            if(userID==null){
                throw new IllegalArgumentException("用户ID不能为空");
            }
            //根据用户id和url加
            String key=userID+":"+request.getRequestURI();
            int count = (Integer) Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse(0);
            int limit = requestLimit.limit();
            int second = requestLimit.second();
            if(count==0){
                redisTemplate.opsForValue().set(key,1,second, TimeUnit.SECONDS);
            } else if (count < limit) {
                redisTemplate.opsForValue().increment(key);
            }else{
                throw new QiyuErrorException(-1,requestLimit.msg());
            }
        }
        return true;
    }
}
