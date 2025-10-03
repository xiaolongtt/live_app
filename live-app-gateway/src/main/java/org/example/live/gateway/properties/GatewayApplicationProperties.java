package org.example.live.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 17:31
 * @注释
 */
@ConfigurationProperties(prefix = "live.gateway")
@Configuration
@RefreshScope
@Data
public class GatewayApplicationProperties {
    /**
     * 主要用来存储url白名单，在nacos配置中心进行配置
     */
    private List<String> notCheckUrlList;
}
