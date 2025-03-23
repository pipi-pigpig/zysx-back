package com.nurturing.Controller;


import com.nurturing.Service.DataService;
import com.nurturing.Service.PersonalHistoryService;
import com.nurturing.entity.DataVO;
import com.nurturing.entity.PersonalHistory;
import com.nurturing.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;
    @Autowired
    private PersonalHistoryService personalHistoryService;

//    @GetMapping("/{user_id}")
//    public Result<DataVO> getbyUserId(@PathVariable long user_id) {
//
//    log.info("根据id查数据:{}", user_id);
//
//    DataVO dataVO=dataService.getByIdWithData(user_id);
//        return Result.success(dataVO);
//    }

    @PostMapping("/data")
    public DataVO getData(@RequestBody Map<String, Long> request) {

        Long user_id= request.get("user_id");
        log.info("根据id查总数据: {}", user_id);
        return dataService.getByIdWithData(user_id);
    }

    @PostMapping("/personal_history")
    public PersonalHistory getPersonalHistory(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查病史: {}", user_id);
        return personalHistoryService.getById(  user_id);
    }

}
