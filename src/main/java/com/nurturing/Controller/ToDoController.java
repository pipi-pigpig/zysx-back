package com.nurturing.Controller;

import com.nurturing.Service.ToDoService;
import com.nurturing.entity.ToDo;
import com.nurturing.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/todos")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;


    @PostMapping("/query")
    public R fetchTodos(@RequestBody Map<String, Object> request){

        String dateString=(String) request.get("date");
        long user_id=((Number) request.get("user_id")).longValue();

        LocalDate date=null;
        if (dateString != null && !dateString.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
             date = LocalDate.parse(dateString, formatter);
        }

        List<ToDo> toDos= toDoService.fetchToDos( date, user_id);;

        return R.success(toDos);
    }

}
