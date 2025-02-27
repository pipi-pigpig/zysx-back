package com.nurturing.Controller;


import com.nurturing.Service.SleepDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/data")
public class SleepDataController {

    @Autowired
    private SleepDataService sleepDataService;


}
