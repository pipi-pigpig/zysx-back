package com.nurturing.Service;

import com.nurturing.entity.BloodData;

import java.util.List;

public interface BloodDataService {
    List<BloodData> getById(long userId);

}
