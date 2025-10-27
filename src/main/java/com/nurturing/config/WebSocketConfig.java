package com.nurturing.config;

import com.nurturing.Handler.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BloodOxygenWebSocketHandler bloodOxygenWebSocketHandler;

    @Autowired
    private HeartRateWebSocketHandler heartRateWebSocketHandler;

    @Autowired
    private PerfusionIndexWebSocketHandler perfusionIndexWebSocketHandler;

    @Autowired
    private BloodPressureWebSocketHandler bloodPressureWebSocketHandler;

    @Autowired
    private BloodSugarWebSocketHandler bloodSugarWebSocketHandler;

    @Autowired
    private SleepDataWebSocketHandler sleepDataWebSocketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 血氧数据WebSocket
        registry.addHandler(bloodOxygenWebSocketHandler, "/websocket/bloodOxygen")
                .setAllowedOrigins("*");

        // 心率数据WebSocket
        registry.addHandler(heartRateWebSocketHandler, "/websocket/heartRate")
                .setAllowedOrigins("*");

        // 灌注指数WebSocket
        registry.addHandler(perfusionIndexWebSocketHandler, "/websocket/perfusionIndex")
                .setAllowedOrigins("*");
       //血压
        registry.addHandler(bloodPressureWebSocketHandler, "/websocket/bloodPressure")
                .setAllowedOrigins("*");
        //睡眠
        registry.addHandler(sleepDataWebSocketHandler, "/websocket/sleepData")
                .setAllowedOrigins("*");

        registry.addHandler(bloodSugarWebSocketHandler, "/websocket/bloodSugar")
                .setAllowedOrigins("*");

    }
}