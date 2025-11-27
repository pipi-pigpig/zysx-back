package com.nurturing.Service.impI;


import com.nurturing.DTO.CreateTodoRequest;
import com.nurturing.DTO.UpdateTodoRequest;
import com.nurturing.Mapper.ToDoMapper;
import com.nurturing.Service.ToDoService;
import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Transactional // 保证主表和子表原子性
    public void createTodo(CreateTodoRequest request) {
        // 1. 验证必要字段
        if (request.getUserId() == null ||
                request.getEventName() == null ||
                request.getTodoType() == null ||
                request.getStartTime() == null ||
                request.getStartDate() == null ||
                request.getEndDate() == null) {
            throw new IllegalArgumentException("Missing required fields");
        }

        String dbTodoType = "medication".equals(request.getTodoType()) ? "用药计划" : "日常安排";

        // 构造主表实体
        ToDo calendar = new ToDo();
        calendar.setUserId(request.getUserId());
        calendar.setTodoType(dbTodoType);
        calendar.setEventName(request.getEventName());
        calendar.setStartDate(LocalDate.parse(request.getStartDate()));
        calendar.setStartTime(LocalTime.parse(request.getStartTime()));
        calendar.setEndDate(LocalDate.parse(request.getEndDate()));
        calendar.setCompleted(request.getCompleted() != null ? request.getCompleted() : 0);

        // 插入主表，id 会自动回填到 calendar 对象
        int rows = toDoMapper.insertTodoCalendar(calendar);
        if (rows != 1) {
            throw new RuntimeException("Failed to insert into todo_calendar");
        }

        Long todoId = calendar.getId(); // ✅ 正确获取自增 ID
        if (todoId == null) {
            throw new RuntimeException("Generated id is null");
        }

        // 插入子表
        if ("medication".equals(request.getTodoType())) {
            toDoMapper.insertMedication(todoId, request.getDosage());
        } else {
            LocalTime endTime = request.getEndTime() != null ?
                    LocalTime.parse(request.getEndTime()) : calendar.getStartTime();
            toDoMapper.insertDailySchedule(todoId, endTime, request.getLocation(), request.getRemarks());
        }
    }

    @Override
    @Transactional
    public void updateTodo(Long id, UpdateTodoRequest request) {
        // 1. 校验必要字段
        if (request.getTodoType() == null) {
            throw new IllegalArgumentException("todoType is required");
        }

        // 2. 映射数据库 todo_type
        String dbTodoType;
        if ("medication".equals(request.getTodoType())) {
            dbTodoType = "用药计划";
        } else if ("schedule".equals(request.getTodoType())) {
            dbTodoType = "日常安排";
        } else {
            throw new IllegalArgumentException("Invalid todoType: " + request.getTodoType());
        }

        // 3. 更新主表
        int updated = toDoMapper.updateTodoCalendar(
                id,
                dbTodoType,
                request.getEventName(),
                request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null,
                request.getStartTime() != null ? LocalTime.parse(request.getStartTime()) : null,
                request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null,
                request.getCompleted()
        );

        if (updated == 0) {
            throw new RuntimeException("Todo not found with id: " + id);
        }

        // 4. 更新子表：先删除旧的，再插入新的（简单可靠）
        if ("medication".equals(request.getTodoType())) {
            toDoMapper.deleteMedicationByTodoId(id);
            if (request.getDosage() != null) {
                toDoMapper.insertMedication(id, request.getDosage());
            }
        } else if ("schedule".equals(request.getTodoType())) {
            toDoMapper.deleteDailyScheduleByTodoId(id);
            LocalTime endTime = null;
            if (request.getEndTime() != null) {
                endTime = LocalTime.parse(request.getEndTime());
            } else if (request.getStartTime() != null) {
                endTime = LocalTime.parse(request.getStartTime()); // fallback
            }
            toDoMapper.insertDailySchedule(id, endTime, request.getLocation(), request.getRemarks());
        }
    }

    @Override
    @Transactional
    public void deleteTodo(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid todo id");
        }

        // 先删除子表记录（无论类型，都尝试删两个表，避免类型判断）
        toDoMapper.deleteMedicationByTodoId(id);
        toDoMapper.deleteDailyScheduleByTodoId(id);

        // 再删除主表
        int deleted = toDoMapper.deleteTodoCalendarById(id);
        if (deleted == 0) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void updateTodoStatus(Long id, Integer completed) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid todo id");
        }
        int updated = toDoMapper.updateTodoCompleted(id, completed);
        if (updated == 0) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
    }
}