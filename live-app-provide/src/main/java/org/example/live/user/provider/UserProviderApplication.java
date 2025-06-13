package org.example.live.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/1/16 18:29
 * @注释 用户中台服务者
 */
/**
 * EnableDubbo该注解用于开启 Dubbo 服务的支持。
 * 使用这个注解后，Spring Boot 应用会自动扫描并加载 Dubbo 相关的配置和服务，使得应用可以作为 Dubbo 服务的提供者或消费者进行服务的发布与调用。
 *
 * EnableDiscoveryClient该注解用于启用服务发现客户端功能。
 * 它允许各个微服务在运行时动态地注册自己的服务实例，并发现其他微服务的实例信息。
 * 使用@EnableDiscoveryClient注解后，SpringBoot 应用会根据所使用的服务发现组件（如 Eureka、Consul、Nacos 等）的配置，
 * 自动注册到服务发现中心，并能够从服务发现中心获取其他服务的实例列表，从而实现微服务之间的通信和调用。
 */

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication {
   public static void main(String[] args){
       SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
       springApplication.setWebApplicationType(WebApplicationType.NONE);
       springApplication.run(args);
   }
}
