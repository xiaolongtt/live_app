package com.example.webstarter.Config;

import com.example.webstarter.Interceptor.LiveUserInfoInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 20:30
 * @注释
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public LiveUserInfoInterceptor liveUserInfoInterceptor() {
        return new LiveUserInfoInterceptor();
    }

    /**
     * 添加自定义拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(liveUserInfoInterceptor()).addPathPatterns("/**").excludePathPatterns("/error");
    }
}
