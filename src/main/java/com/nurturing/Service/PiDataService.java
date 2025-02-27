package com.nurturing.Service;

import com.nurturing.entity.PiData;

import java.util.List;

public interface PiDataService {
    List<PiData> getById(Long userId);
}
