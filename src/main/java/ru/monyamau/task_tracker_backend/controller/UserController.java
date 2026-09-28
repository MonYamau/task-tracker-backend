package ru.monyamau.task_tracker_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.UserApi;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;

@RestController
public class UserController implements UserApi {
    @Override
    public ResponseEntity<Void> register(UserRequestDto requestDto) {
        return null;
    }

    @Override
    public ResponseEntity<UserResponseDto> show() {
        return null;
    }
}
