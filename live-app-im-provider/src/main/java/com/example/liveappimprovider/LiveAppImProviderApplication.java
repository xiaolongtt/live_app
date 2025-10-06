package com.example.liveappimprovider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class LiveAppImProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveAppImProviderApplication.class, args);
    }

}
