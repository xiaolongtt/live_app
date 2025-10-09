package com.example.liveappimcoreserver;

import com.example.liveappimcoreserver.common.ImMsgDecoder;
import com.example.liveappimcoreserver.common.ImMsgEncoder;
import com.example.liveappimcoreserver.handler.ImCoreServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


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
