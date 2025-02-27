package com.nurturing.Service;

import com.nurturing.entity.HeartData;

import java.util.List;

public interface HeartDataService {

    List<HeartData> getById(long userId);

}
