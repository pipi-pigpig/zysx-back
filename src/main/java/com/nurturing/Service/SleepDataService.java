package com.nurturing.Service;

import com.nurturing.entity.SleepData;

import java.util.List;

public interface SleepDataService {
    List<SleepData> getById(Long userId);
}
