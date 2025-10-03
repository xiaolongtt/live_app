package com.example.webstarter.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.live.common.interfaces.Enum.GatewayHeaderEnum;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 18:55
 * @注释 自定义拦截器，用于在请求处理之前获取用户信息并设置到ThreadLocal中，方便后续方法使用userid
 */
public class LiveUserInfoInterceptor implements HandlerInterceptor {

    /**
     * 主要是在请求处理之前获取用户信息并设置到ThreadLocal中
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先获取在请求头中储存的userid
        String headers_userID = request.getHeader(GatewayHeaderEnum.USER_LOGIN_ID.getName());
        //可能是走的白名单
        if(headers_userID==null || headers_userID.isEmpty()){
            return true;
        }
        //将用户id存入线程中，有利于后续使用
        LiveRequestContext.set(RequestConstants.LIVE_USER_ID, headers_userID);
        return true;
    }

    /**
     * 主要是在请求处理之后，将ThreadLocal中的用户信息清除，防止内存泄漏
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LiveRequestContext.clear();
    }
}
