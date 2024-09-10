package com.mc.wsdemo.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * ClassName: WebSocketConfig
 * Package: com.mc.wsdemo.java
 * Description:
 *
 * @Author shidongyang
 * @Create 2024/9/8 10:48
 * @Version 1.0
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter () {
        return new ServerEndpointExporter();
    }
}
