package com.example.mqstarter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 14:41
 * @注释 RocketMQ 生产者配置属性
 */
@ConfigurationProperties(prefix = "rocketmq.producer")
@Configuration
@Data
public class RocketMQProducerProperties {
    private String nameSrv;             // NameServer 地址，如 "192.168.1.100:9876"
    private String groupName;           // 生产者组名，用于标识一组生产者
    private String applicationName;     // 应用名称，用于监控和日志
    private Integer sendMsgTimeout;     // 发送消息超时时间（毫秒）
    private Integer retryTimes;         // 发送失败重试次数
}
