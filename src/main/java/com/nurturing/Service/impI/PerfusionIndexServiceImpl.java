package com.nurturing.Service.impI;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Mapper.PerfusionIndexMapper;
import com.nurturing.Service.PerfusionIndexService;
import com.nurturing.entity.PerfusionIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PerfusionIndexServiceImpl extends ServiceImpl<PerfusionIndexMapper,PerfusionIndex> implements PerfusionIndexService {


    @Autowired
    private PerfusionIndexMapper perfusionIndexMapper;


    @Override
    public List<PerfusionIndex> getById(Long userId) {
        return perfusionIndexMapper.getById(userId);
    }

    @Override
    @Transactional
    public void saveFromMQTT(PerfusionIndex perfusionIndex, String mac) {
        perfusionIndexMapper.insertByMac(perfusionIndex.getPiData(),mac, LocalDateTime.now());
    }
}
