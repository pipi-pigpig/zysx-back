package com.nurturing.Service;

import com.nurturing.entity.PressureData;

import java.util.List;

public interface PressureDataService {
    List<PressureData> getById(Long userId);

}
