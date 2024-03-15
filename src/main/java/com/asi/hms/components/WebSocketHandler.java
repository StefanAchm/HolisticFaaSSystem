package com.asi.hms.components;

import com.asi.hms.service.WebSocketSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionService sessionService;

    @Autowired
    public WebSocketHandler(WebSocketSessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        this.sessionService.registerSession(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        this.sessionService.unregisterSession(session);
    }

}
