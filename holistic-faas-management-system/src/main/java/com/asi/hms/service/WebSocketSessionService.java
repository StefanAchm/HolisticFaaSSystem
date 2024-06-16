package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Message;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketSessionService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionService.class);

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    public void registerSession(WebSocketSession session) {
        logger.info("Registered session: {}", session.getId());
        sessions.put(session.getId(), session);
    }

    public void unregisterSession(WebSocketSession session) {

        try (WebSocketSession ignored = sessions.remove(session.getId())) {
            logger.info("Unregistered session: {}", session.getId());
        } catch (IOException e) {
            throw new HolisticFaaSException(e);
        }

    }

    private void sendMessage(String sessionId, Message message) {

        WebSocketSession session = sessions.get(sessionId);

        if (session != null && session.isOpen()) {

            try {

                Gson gson = new Gson();
                String textMessage = gson.toJson(message);

                synchronized (session) {
                    session.sendMessage(new TextMessage(textMessage));
                }


            } catch (IOException e) {

                throw new HolisticFaaSException(e);
            }

        }

    }

    public void sendMessage(Message message) {

        if(sessions.isEmpty()) {
            logger.warn("No sessions to send message to");
            return;
        }

        logger.info("Sending message ({} sessions): {}", sessions.size(), message);

        sessions.values().forEach(session -> sendMessage(session.getId(), message));

    }

}
