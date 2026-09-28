package ru.monyamau.task_tracker_backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;

import java.util.List;

@RequestMapping("/tasks")
public interface TaskCollectionApi {
    @GetMapping
    ResponseEntity<List<TaskResponseDto>> show();
}
