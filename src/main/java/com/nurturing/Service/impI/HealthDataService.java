package com.nurturing.Service.impI;

import com.nurturing.Mapper.HealthDataMapper;
import com.nurturing.entity.HealthData;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
            int nextId = currentId.getAndUpdate(prev -> prev % 10 + 1);
            data.setId(nextId);
            mapper.replace(data);
        }
    }

    public void addToBuffer(HealthData data) {
        buffer.offer(data);
    }
}