package ru.monyamau.task_tracker_backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;

@RequestMapping("/user")
public interface UserApi {
    @PostMapping
    ResponseEntity<Void> register(UserRequestDto requestDto);

    @GetMapping
    ResponseEntity<UserResponseDto> show();
}
