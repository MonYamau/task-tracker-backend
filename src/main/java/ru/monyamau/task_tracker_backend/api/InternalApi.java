package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.PeriodTimeRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskInternalResponseDto;

import java.util.List;

@Hidden
@RequestMapping("/internal")
public interface InternalApi {
    @GetMapping("/period-tasks")
    ResponseEntity<List<TaskInternalResponseDto>> show(@Valid @RequestBody PeriodTimeRequestDto requestDto);
}