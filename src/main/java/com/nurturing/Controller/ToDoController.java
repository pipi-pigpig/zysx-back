package com.nurturing.Controller;

import com.nurturing.DTO.QueryTodoRequest;
import com.nurturing.Service.ToDoService;
import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;
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
    public R fetchTodos(@RequestBody QueryTodoRequest request){

        LocalDate startDate = LocalDate.parse(request.getStartDate());
        List<ToDoVo> todos = toDoService.fetchToDos(startDate, request.getUserId());
        return R.success(todos);
    }
    @PostMapping("/create")
    public R create(@RequestBody ToDo toDo){

        ToDo toDo1=toDoService.insert(toDo);
        return R.success(toDo1);
    }

}
