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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


/**
 * 作为IM核心服务端启动类
 */
@SpringBootApplication
public class LiveAppImCoreServerApplication {

    @Resource
    private ApplicationContext applicationContext;

    //定义服务端口
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    //基于netty去启动一个java进程，绑定监听的端口
    public void startApplication(int port) throws InterruptedException {
        setPort(port);
        //用来处理accept事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //用来处理reda和write事件
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        //初始化netty相关的handler
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //添加编码器和解码器
                channel.pipeline().addLast(applicationContext.getBean(ImMsgEncoder.class));
                channel.pipeline().addLast(applicationContext.getBean(ImMsgDecoder.class));
                //添加自定义的消息处理器
                channel.pipeline().addLast(applicationContext.getBean(ImCoreServerHandler.class));
                System.out.println("initChannel");
            }
        });
        //基于JVM的钩子函数实现关闭
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
           bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
        }));

        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        //这里会同步阻塞主线程，实现服务长期开启的效果
        channelFuture.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        LiveAppImCoreServerApplication liveAppImCoreServerApplication = new LiveAppImCoreServerApplication();
        liveAppImCoreServerApplication.startApplication(9090);

    }

}
