package com.example.liveappimcoreserver.stater;

import com.example.liveappimcoreserver.common.ChannelHandlerContextCache;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 15:59
 * @注释 IM核心服务端启动器
 */
public class ImCoreServerStater implements InitializingBean {

    //定义服务端口
    @Value("${qiyu.im.ws.port}")
    private int port;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private Environment environment;

    //基于netty去启动一个java进程，绑定监听的端口
    public void startApplication() throws InterruptedException {
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
        //获取im的服务注册ip和暴露端口
        //在启动时配置这两个参数，表示这个服务器的注册ip和端口
        String dubboIpToRegistry = environment.getProperty("DUBBO_IP_TO_REGISTRY");
        String dubboPortToRegistry = environment.getProperty("DUBBO_PORT_TO_REGISTRY");
        if(StringUtils.isEmpty(dubboPortToRegistry)||StringUtils.isEmpty(dubboIpToRegistry)){
            throw new IllegalArgumentException("启动参数中的注册端口和注册ip不能为空");
        }
        //先将服务器ip地址缓存下来
        ChannelHandlerContextCache.setServerIpAddress(dubboIpToRegistry+":"+dubboPortToRegistry);
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        //这里会同步阻塞主线程，实现服务长期开启的效果
        channelFuture.channel().closeFuture().sync();
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Thread nettyServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startApplication();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        nettyServerThread.setName("live-app-im-server");
        nettyServerThread.start();
    }
}
