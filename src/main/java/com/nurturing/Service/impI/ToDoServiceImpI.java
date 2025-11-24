package com.nurturing.Service.impI;


import com.nurturing.Mapper.ToDoMapper;
import com.nurturing.Service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ToDoServiceImpI implements ToDoService {

    @Autowired
    private ToDoMapper toDoMapper;
}
