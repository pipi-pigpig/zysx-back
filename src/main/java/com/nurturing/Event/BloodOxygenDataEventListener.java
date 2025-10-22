package com.nurturing.Event;

import com.nurturing.Handler.BloodOxygenWebSocketHandler;
import com.nurturing.entity.BloodOxygen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午8:58
 */
@Component
@Slf4j
public class BloodOxygenDataEventListener {

    @Autowired
    private BloodOxygenWebSocketHandler webSocketHandler;

    @EventListener
    public void handleBloodOxygenDataEvent(BloodOxygenDataEvent event) {
        BloodOxygen bloodOxygen = event.getBloodOxygen();
        List<BloodOxygen> dataList = Collections.singletonList(bloodOxygen);
        webSocketHandler.pushDataToUser(String.valueOf(bloodOxygen.getUserId()), dataList);
        log.info("已推送血氧数据给用户: {}", bloodOxygen.getUserId());
    }
}
