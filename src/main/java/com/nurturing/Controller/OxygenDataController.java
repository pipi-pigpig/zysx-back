package com.nurturing.Controller;


import com.nurturing.Service.OxygenDataService;
import com.nurturing.entity.OxygenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/oxygenData")
public class OxygenDataController {

    @Autowired
    private OxygenDataService oxygenDataService;

    @GetMapping("/{user_id}")
    public List<OxygenData> getOxygenData(@PathVariable long user_id) {

        log.info("根据id查血氧：{}",user_id);
        List<OxygenData> oxygenData=oxygenDataService.getById(user_id);
        return oxygenData ;
    }


}
