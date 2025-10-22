package com.nurturing.config;

import com.nurturing.Handler.BloodOxygenWebSocketHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(bloodOxygenWebSocketHandler(), "/websocket/oxygen")
                .setAllowedOrigins("*");
    }

    @Bean
    public BloodOxygenWebSocketHandler bloodOxygenWebSocketHandler() {
        return new BloodOxygenWebSocketHandler();
    }
}