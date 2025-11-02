package com.example.livebankapi;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 13:32
 * @注释
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class liveBankApiApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(liveBankApiApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        springApplication.run(args);
    }
}
