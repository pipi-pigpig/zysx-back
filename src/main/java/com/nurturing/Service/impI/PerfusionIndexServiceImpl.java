package com.nurturing.Service.impI;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Event.PerfusionIndexDataEvent;
import com.nurturing.Mapper.PerfusionIndexMapper;
import com.nurturing.Service.PerfusionIndexService;
import com.nurturing.entity.PerfusionIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PerfusionIndexServiceImpl extends ServiceImpl<PerfusionIndexMapper,PerfusionIndex>
        implements PerfusionIndexService {

    @Autowired
    private PerfusionIndexMapper perfusionIndexMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<PerfusionIndex> getById(Long userId) {
        return perfusionIndexMapper.getById(userId);
    }

    @Override
    public List<PerfusionIndex> getRecentData(Long userId) {
        return perfusionIndexMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(PerfusionIndex entity) {
        boolean result = super.save(entity);
        if (result) {
            // 可以创建PerfusionIndexDataEvent并发布事件
             eventPublisher.publishEvent(new PerfusionIndexDataEvent(this, entity));
        }
        return result;
    }

    @Override
    @Transactional
    public void saveHealthData(PerfusionIndex entity) {
        save(entity);
        // 如果需要实时推送，取消上面的注释
         eventPublisher.publishEvent(new PerfusionIndexDataEvent(this, entity));
    }
    @Override
    @Transactional
    public void saveFromMQTT(PerfusionIndex perfusionIndex, String mac) {
        perfusionIndexMapper.insertByMac(perfusionIndex.getPiData(),mac, LocalDateTime.now());
    }
}
