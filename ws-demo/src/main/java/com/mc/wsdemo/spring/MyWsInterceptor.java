package com.mc.wsdemo.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * ClassName: MyWsInterceptor
 * Package: com.mc.wsdemo.spring
 * Description:
 *
 * @Author shidongyang
 * @Create 2024/9/8 12:50
 * @Version 1.0
 */
/**
 * 握手拦截
 * */
@Component
@Slf4j
public class MyWsInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info(request.getRemoteAddress().getAddress() + "开始握手");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info(request.getRemoteAddress().getAddress() + "握手完成");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
