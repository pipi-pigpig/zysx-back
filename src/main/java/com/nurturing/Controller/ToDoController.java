package com.nurturing.Controller;

import com.nurturing.DTO.CreateTodoRequest;
import com.nurturing.DTO.QueryTodoRequest;
import com.nurturing.DTO.UpdateTodoRequest;
import com.nurturing.DTO.UpdateTodoStatusRequest;
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
@RequestMapping("/api/todos")
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
    public R createTodo(@RequestBody CreateTodoRequest request) {
        try {
            toDoService.createTodo(request);
            if (request.getTodoType().equals("medication")){
                return R.success("插入用药计划成功");
            }
            else return R.success("插入日程计划成功");


        } catch (Exception e) {
            return R.error(0,"Failed to create todo: " + e.toString());
        }
    }


    @PostMapping("/update/{id}")
    public R updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest request) {
        try {
            toDoService.updateTodo(id, request);
            return R.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace(); // 建议用 logger
            return R.error(0,"Failed to update todo: " + e.toString());
        }
    }

    @PostMapping("/delete/{id}")
    public R deleteTodo(@PathVariable Long id) {
        try {
            toDoService.deleteTodo(id);
            return R.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(0,"Failed to delete todo: " + e.getMessage());
        }
    }

    @PostMapping("/update-status/{id}")
    public R updateTodoStatus(@PathVariable Long id, @RequestBody UpdateTodoStatusRequest request) {
        try {
            if (request.getCompleted() == null) {
                return R.error(0,"completed field is required");
            }
            if (request.getCompleted() != 0 && request.getCompleted() != 1) {
                return R.error(0,"completed must be 0 or 1");
            }
            toDoService.updateTodoStatus(id, request.getCompleted());
            return R.success("更新状态成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(0,"Failed to update status: " + e.getMessage());
        }
    }
}
