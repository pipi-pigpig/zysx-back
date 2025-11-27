package com.nurturing.Service;

import com.nurturing.DTO.CreateTodoRequest;
import com.nurturing.DTO.UpdateTodoRequest;
import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ToDoService {
    List<ToDoVo> fetchToDos(LocalDate startDate, Long userId);


    void createTodo(CreateTodoRequest request);

    void updateTodo(Long id, UpdateTodoRequest request);

    void deleteTodo(Long id);

    void updateTodoStatus(Long id, Integer completed);
}
