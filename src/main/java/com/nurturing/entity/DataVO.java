package com.nurturing.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class DataVO {

    private long id;
    private String username;
    private String password;
    private String account;
    private String avatar;
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;

    private List<BloodSugar> bloodData=new ArrayList<>();
    private List<HeartRate> heartData=new ArrayList<>() ;
    private List<BloodOxygen> bloodOxygenData =new ArrayList<>();
    private List<PerfusionIndex> piData=new ArrayList<>();
    private List<BloodPressure> bloodPressureData =new ArrayList<>();
    private List<SleepData> sleepData=new ArrayList<>();

}
