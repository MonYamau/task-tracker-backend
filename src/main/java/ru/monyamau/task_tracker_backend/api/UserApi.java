package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

@RequestMapping("/user")
public interface UserApi {
    @PostMapping
    ResponseEntity<Void> register(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                  UserRequestDto requestDto);

    @GetMapping
    ResponseEntity<UserResponseDto> show(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal);
}
