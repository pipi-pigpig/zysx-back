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
public class DataServiceImpl implements  DataService {


    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private BloodSugarMapper bloodSugarMapper;
    @Autowired
    private HeartRateMapper heartRateMapper;
    @Autowired
    private BloodOxygenMapper bloodOxygenMapper;
    @Autowired
    private PerfusionIndexMapper perfusionIndexMapper;
    @Autowired
    private BloodPressureMapper bloodPressureMapper;
    @Autowired
    private  SleepDataMapper sleepDataMapper;


    @Override
    public DataVO getByIdWithData(Long userId) {

    User user= dataMapper.getById(userId);
    DataVO dataVO = new DataVO();
    BeanUtils.copyProperties(user,dataVO);

    List<BloodSugar> bloodData= bloodSugarMapper.getById(userId);
    dataVO.setBloodData(bloodData);

    List<HeartRate> heartData = heartRateMapper.getById(userId);
    dataVO.setHeartData(heartData);

    List<BloodOxygen> bloodOxygenData = bloodOxygenMapper.getById(userId);
    dataVO.setBloodOxygenData(bloodOxygenData);

    List<PerfusionIndex> piData = perfusionIndexMapper.getById(userId);
    dataVO.setPiData(piData);

    List<BloodPressure> bloodPressureData = bloodPressureMapper.getById(userId);
    dataVO.setBloodPressureData(bloodPressureData);

    List<SleepData> sleepData =sleepDataMapper.getById(userId);
    dataVO.setSleepData(sleepData);

        return dataVO;
    }
}
