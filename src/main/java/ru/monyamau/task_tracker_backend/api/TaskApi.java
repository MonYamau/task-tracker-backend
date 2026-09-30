package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.TaskModificationRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

@RequestMapping("/task")
public interface TaskApi {
    @PostMapping
    ResponseEntity<TaskResponseDto> create(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                           TaskRequestDto requestDto);

    @PostMapping("/edit")
    ResponseEntity<TaskResponseDto> edit(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                         TaskModificationRequestDto requestDto);

    @DeleteMapping
    ResponseEntity<Void> delete(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                TaskRequestDto requestDto);
}
