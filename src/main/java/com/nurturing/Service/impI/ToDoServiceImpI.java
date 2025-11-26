package com.nurturing.Service.impI;


import com.nurturing.Mapper.ToDoMapper;
import com.nurturing.Service.ToDoService;
import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ToDoServiceImpI implements ToDoService {

    @Autowired
    private ToDoMapper toDoMapper;

    @Override
    public List<ToDoVo> fetchToDos(LocalDate startDate, Long userId) {
        return toDoMapper.getToDos(startDate, userId);
    }

    @Override
    public ToDo insert(ToDo toDo) {
        return toDoMapper.insert(toDo);
    }
}
