package com.mc.wsdemo.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: WsServerEndPoint
 * Package: com.mc.wsdemo.java
 * Description:
 *
 * @Author shidongyang
 * @Create 2024/9/8 10:24
 * @Version 1.0
 */

@ServerEndpoint("/myWs")
@Component
@Slf4j
public class WsServerEndPoint {

    static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    // 监听打开链接
    @OnOpen
    public void onOpen (Session session) {
        sessionMap.put(session.getId(), session);
        log.info("websocket is open");
    }

    // 监听收到消息
    @OnMessage
    public String onMessage (String text) {
        log.info("收到消息了：" + text);
        return "已经收到消息";
    }

    // 监听关闭
    @OnClose
    public void onClose (Session session) {
        sessionMap.remove(session.getId());
        log.info("websocket is close");
    }

    // 定时任务
    @Scheduled(fixedRate = 2000)
    public void sendMsg () throws IOException {
        for (String key:sessionMap.keySet()) {
            sessionMap.get(key).getBasicRemote().sendText("心跳");
        }
    }
}
