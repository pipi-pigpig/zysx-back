package com.nurturing.Controller;


import com.nurturing.Service.DataService;
import com.nurturing.entity.DataVO;
import com.nurturing.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

//    @GetMapping("/{user_id}")
//    public Result<DataVO> getbyUserId(@PathVariable long user_id) {
//
//    log.info("根据id查数据:{}", user_id);
//
//    DataVO dataVO=dataService.getByIdWithData(user_id);
//        return Result.success(dataVO);
//    }

}
