package com.example.livebankprovider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 9:51
 * @注释
 */
@SpringBootApplication
@EnableDubbo
public class BankProviderApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BankProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
