package com.example.mqstarter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/7 14:44
 * @注释 在这里将rocketmq的消费者的配置信息进行封装
 */
@Configuration
@ConfigurationProperties(prefix = "rocketmq.consumer")
@Data
public class RocketMQConsumerProperties {
    private String nameSrv;
    private String groupName;
}
