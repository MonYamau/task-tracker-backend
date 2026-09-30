package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

import java.util.List;

@RequestMapping("/tasks")
public interface TaskCollectionApi {
    @GetMapping
    ResponseEntity<List<TaskResponseDto>> show(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal);
}
