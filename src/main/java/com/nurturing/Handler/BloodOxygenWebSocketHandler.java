package com.nurturing.Handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurturing.Service.BloodOxygenService;
import com.nurturing.entity.BloodOxygen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午8:49
 */
@Component
public class BloodOxygenWebSocketHandler extends BaseHealthDataWebSocketHandler<BloodOxygen> {

    @Autowired
    private BloodOxygenService bloodOxygenService;

    @Override
    protected List<BloodOxygen> getRecentData(Long userId) {
        return bloodOxygenService.getRecentData(userId);
    }

    @Override
    protected String getDataType() {
        return "bloodOxygen";
    }
}