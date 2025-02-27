package com.nurturing.Service;

import com.nurturing.entity.OxygenData;

import java.util.List;

public interface OxygenDataService {
    List<OxygenData> getById(long userId);

}
