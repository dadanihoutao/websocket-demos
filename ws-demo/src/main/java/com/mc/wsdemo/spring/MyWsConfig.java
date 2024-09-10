package com.mc.wsdemo.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * ClassName: MyWsConfig
 * Package: com.mc.wsdemo.spring
 * Description:
 *
 * @Author shidongyang
 * @Create 2024/9/9 16:11
 * @Version 1.0
 */

@Configuration
@EnableWebSocket
public class MyWsConfig implements WebSocketConfigurer {

    @Resource
    MyWsHandler myWsHandler;

    @Resource
    MyWsInterceptor myWsInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWsHandler, "/myWs1").addInterceptors(myWsInterceptor).setAllowedOrigins("*");
    }
}
