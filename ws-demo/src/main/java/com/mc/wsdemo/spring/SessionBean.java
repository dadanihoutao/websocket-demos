package com.mc.wsdemo.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * ClassName: SessionBean
 * Package: com.mc.wsdemo.spring
 * Description:
 *
 * @Author shidongyang
 * @Create 2024/9/8 13:03
 * @Version 1.0
 */

@AllArgsConstructor
@Data
public class SessionBean {
    private WebSocketSession webSocketSession;
    private Integer clientId;
}
