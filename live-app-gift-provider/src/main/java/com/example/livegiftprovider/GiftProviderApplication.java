package com.example.livegiftprovider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:46
 * @注释
 */
@SpringBootApplication
@EnableDubbo
public class GiftProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

}