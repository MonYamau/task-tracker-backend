package ru.monyamau.task_tracker_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.TaskCollectionApi;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.TaskCollectionService;

import java.util.List;

@RestController
public class TaskCollectionController implements TaskCollectionApi {
    private final TaskCollectionService taskCollectionService;

    @Autowired
    public TaskCollectionController(TaskCollectionService taskCollectionService) {
        this.taskCollectionService = taskCollectionService;
    }

    @Override
    public ResponseEntity<List<TaskResponseDto>> show(UserPrincipal userPrincipal) {
        List<TaskResponseDto> responseDtoList = taskCollectionService.findAll(userPrincipal.id());
        return ResponseEntity.ok(responseDtoList);
    }
}