package ru.monyamau.task_tracker_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.TaskApi;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.TaskService;

@RestController
public class TaskController implements TaskApi {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ResponseEntity<TaskResponseDto> show(UserPrincipal userPrincipal, TaskRefRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.findTask(userPrincipal.id(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<TaskResponseDto> create(UserPrincipal userPrincipal, TaskFormRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.saveTask(userPrincipal.id(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Override
    public ResponseEntity<TaskResponseDto> edit(UserPrincipal userPrincipal, TaskRefRequestDto refRequestDto,
                                                TaskFormRequestDto formRequestDto) {
        TaskResponseDto responseDto = taskService.updateTask(userPrincipal.id(), refRequestDto, formRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<Void> delete(UserPrincipal userPrincipal, TaskRefRequestDto requestDto) {
        taskService.deleteTask(userPrincipal.id(), requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}