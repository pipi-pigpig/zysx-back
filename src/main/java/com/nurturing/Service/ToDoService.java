package com.nurturing.Service;

import com.nurturing.entity.ToDo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ToDoService {
    List<ToDo> fetchToDos(LocalDate date, long userId);
}
