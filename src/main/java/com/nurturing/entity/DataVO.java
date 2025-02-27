package com.nurturing.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class DataVO {

    private long user_id;
    private String username;
    private String password;
    private String image;
    private String birthdate;
    private String height;
    private String weight;
    private LocalDateTime created_at ;

    private List<BloodData> bloodData=new ArrayList<>();
    private List<HeartData> heartData=new ArrayList<>() ;
    private List<OxygenData> oxygenData=new ArrayList<>();
    private List<PiData> piData=new ArrayList<>();
    private List<PressureData> pressureData=new ArrayList<>();
    private List<SleepData> sleepData=new ArrayList<>();

}
