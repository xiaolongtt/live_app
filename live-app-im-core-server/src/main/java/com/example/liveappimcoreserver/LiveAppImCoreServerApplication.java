package com.example.liveappimcoreserver;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 作为IM核心服务端启动类
 */
@SpringBootApplication
@EnableDubbo
public class LiveAppImCoreServerApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication springApplication = new SpringApplication(LiveAppImCoreServerApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);

    }

}
