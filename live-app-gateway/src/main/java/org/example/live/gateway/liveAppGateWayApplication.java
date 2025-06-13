package org.example.live.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 15:22
 * @注释
 */
@SpringBootApplication
@EnableDiscoveryClient
public class liveAppGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(liveAppGateWayApplication.class, args);
    }
}

