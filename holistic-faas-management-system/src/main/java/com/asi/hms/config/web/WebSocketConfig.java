package com.asi.hms.config.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    private final WebSocketHandler myHandler;

    @PostConstruct
    public void init() {
        logger.info("cors.allowed.origins: {}", (Object) allowedOrigins);
    }

    @Autowired
    public WebSocketConfig(WebSocketHandler myHandler) {
        this.myHandler = myHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(myHandler, "/websocket")
                .setAllowedOrigins(allowedOrigins);

    }

}
