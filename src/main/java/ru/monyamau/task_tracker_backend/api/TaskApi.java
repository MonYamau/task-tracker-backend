package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

@RequestMapping("/task")
public interface TaskApi {
    @GetMapping
    ResponseEntity<TaskResponseDto> show(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                         @ModelAttribute(name = "id") TaskRefRequestDto requestDto);

    @PostMapping
    ResponseEntity<TaskResponseDto> create(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                           @RequestBody TaskFormRequestDto requestDto);

    @PostMapping("/edit")
    ResponseEntity<TaskResponseDto> edit(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                         @ModelAttribute(name = "id") TaskRefRequestDto refRequestDto,
                                         @RequestBody TaskFormRequestDto formRequestDto);

    @DeleteMapping
    ResponseEntity<Void> delete(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                @ModelAttribute(name = "id") TaskRefRequestDto requestDto);
}
