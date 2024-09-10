package com.mc.wsdemo.spring;

/**
 * ClassName: MyWsHandler
 * Package: com.mc.wsdemo.spring
 * Description:
 *
 * @Author shidongyang
 * @Create 2024/9/8 12:58
 * @Version 1.0
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket 主处理程序
 * */
@Component
@Slf4j
public class MyWsHandler extends AbstractWebSocketHandler {
    private static Map<String, SessionBean> sessionBeanMap;
    private static AtomicInteger clientIdMake;

    private static StringBuffer stringBuffer;

    static  {
        sessionBeanMap = new ConcurrentHashMap<>();
        clientIdMake = new AtomicInteger(0);
        stringBuffer = new StringBuffer();
    }
    // 建立链接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionBean sessionBean = new SessionBean(session, clientIdMake.getAndIncrement());
        sessionBeanMap.put(session.getId(), sessionBean);
        log.info(sessionBeanMap.get(session.getId()).getClientId() + "打开了链接");
        stringBuffer.append(sessionBeanMap.get(session.getId()).getClientId() + ": 进入了群聊<br/>");
        sendMessage(sessionBeanMap);
    }

    // 收到信息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info(sessionBeanMap.get(session.getId()).getClientId() + ":" + message.getPayload());
        stringBuffer.append(sessionBeanMap.get(session.getId()).getClientId() + ":" + message.getPayload() + "<br/>");
        sendMessage(sessionBeanMap);
    }

    // 传输出现异常
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        if (session.isOpen()) {
            sessionBeanMap.remove(session.getId());
        }
    }

    // 链接关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        int clientId = sessionBeanMap.get(session.getId()).getClientId();
        sessionBeanMap.remove(session);
        log.info(clientId + "关闭了链接");
        stringBuffer.append(clientId + ":" + "退出了群聊<br/>");
        sendMessage(sessionBeanMap);
    }


    // 定时任务
    // @Scheduled(fixedRate = 2000)
    // public void sendMsg () throws IOException {
    //     for (String key:sessionBeanMap.keySet()) {
    //         sessionBeanMap.get(key).getWebSocketSession().sendMessage(new TextMessage("心跳---"));
    //     }
    // }

    // 公共的发送方法
    public void sendMessage (Map<String, SessionBean> sessionBeanMap) {

        for (String key: sessionBeanMap.keySet()) {
            try {
                sessionBeanMap.get(key).getWebSocketSession().sendMessage(new TextMessage(stringBuffer.toString()));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
