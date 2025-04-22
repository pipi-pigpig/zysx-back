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
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataMapper mapper;
    private final Queue<HealthData> buffer = new ConcurrentLinkedQueue<>();

    @Scheduled(fixedRate = 5000)
    public void processBuffer() {
        while (!buffer.isEmpty()) {
            HealthData data = buffer.poll();
            if (data == null || data.getUserId() == null) continue;

            // 处理血氧数据
            processData(data,
                    mapper::countBloodOxygenByUser,
                    mapper::deleteOldestBloodOxygen,
                    mapper::insertBloodOxygen);

            // 处理心率数据
            processData(data,
                    mapper::countHeartRateByUser,
                    mapper::deleteOldestHeartRate,
                    mapper::insertHeartRate);

            // 处理PI数据
            processData(data,
                    mapper::countPerfusionIndexByUser,
                    mapper::deleteOldestPerfusionIndex,
                    mapper::insertPerfusionIndex);
        }
    }

    private void processData(HealthData data,
                             Function<Integer, Integer> countFunc,
                             Consumer<Integer> deleteFunc,
                             Consumer<HealthData> insertFunc) {
        Integer userId = data.getUserId();
        int count = countFunc.apply(userId);
        if (count >= 10) {
            deleteFunc.accept(userId);
        }
        insertFunc.accept(data);
    }

    public void addToBuffer(HealthData data) {
        buffer.offer(data);
    }
}