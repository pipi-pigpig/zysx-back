package com.nurturing.Service.impI;

import com.nurturing.Mapper.*;
import com.nurturing.Service.DataService;
import com.nurturing.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class DataServiceImpI implements DataService {


    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private BloodDataMapper bloodDataMapper;
    @Autowired
    private HeartDataMapper heartDataMapper;
    @Autowired
    private OxygenDataMapper oxygenDataMapper;
    @Autowired
    private  PiDataMapper piDataMapper;
    @Autowired
    private  PressureDataMapper pressureDataMapper;
    @Autowired
    private  SleepDataMapper sleepDataMapper;


    @Override
    public DataVO getByIdWithData(Long userId) {

    User user= dataMapper.getById(userId);
    DataVO dataVO = new DataVO();
    BeanUtils.copyProperties(user,dataVO);

    List<BloodData> bloodData= bloodDataMapper.getById(userId);
    dataVO.setBloodData(bloodData);

    List<HeartData> heartData =heartDataMapper.getById(userId);
    dataVO.setHeartData(heartData);

    List<OxygenData> oxygenData =oxygenDataMapper.getById(userId);
    dataVO.setOxygenData(oxygenData);

    List<PiData> piData =piDataMapper.getById(userId);
    dataVO.setPiData(piData);

    List<PressureData> pressureData =pressureDataMapper.getById(userId);
    dataVO.setPressureData(pressureData);

    List<SleepData> sleepData =sleepDataMapper.getById(userId);
    dataVO.setSleepData(sleepData);

        return dataVO;
    }
}
