package com.nurturing.Service.impI;

import com.nurturing.Mapper.HealthDataMapper;
import com.nurturing.entity.HealthData;


import com.nurturing.entity.HeartData;
import com.nurturing.entity.OxygenData;
import com.nurturing.entity.PiData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataMapper mapper;
    private final Queue<HealthData> buffer = new ConcurrentLinkedQueue<>();
    private final AtomicInteger currentId = new AtomicInteger(1);

    @Scheduled(fixedRate = 5000)
    public void processBuffer() {
        if (!buffer.isEmpty()) {
            HealthData data = buffer.poll();
            System.out.println(data);

            int nextId = currentId.getAndUpdate(prev -> prev % 10 + 1);
            data.setId(nextId);
            mapper.replace(data);

            OxygenData oxygenData = new OxygenData();
            oxygenData.setUser_id(data.getUser_id());
            oxygenData.setCreated_at(data.getRecordTime());
            oxygenData.setOxygenData( data.getSpo2());
            oxygenData.setBloodOxygenID(nextId);
            mapper.replaceOxygen(oxygenData);

            HeartData heartData = new HeartData();
            heartData.setUser_id(data.getUser_id());
            heartData.setCreated_at(data.getRecordTime());
            heartData.setHeartData(data.getBmp());
            heartData.setHeartRateID(nextId);
            mapper.replaceHeart(heartData);

            PiData piData = new PiData();
            piData.setUser_id(data.getUser_id());
            piData.setCreated_at(data.getRecordTime());
            piData.setPiData(data.getPi());
            piData.setPerfusionIndexID(nextId);
            mapper.replacePi(piData);

        }
    }

    public void addToBuffer(HealthData data) {
        buffer.offer(data);
    }
}