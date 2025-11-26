package com.nurturing.Service;

import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ToDoService {
    List<ToDoVo> fetchToDos(LocalDate startDate, Long userId);

    ToDo insert(ToDo toDo);
}
