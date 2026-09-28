package ru.monyamau.task_tracker_backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.TaskModificationRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;

@RequestMapping("/task")
public interface TaskApi {
    @PostMapping
    ResponseEntity<TaskResponseDto> create(TaskRequestDto requestDto);

    @PostMapping("/edit")
    ResponseEntity<TaskResponseDto> edit(TaskModificationRequestDto requestDto);

    @DeleteMapping
    ResponseEntity<Void> delete(TaskRequestDto requestDto);
}
