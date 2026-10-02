package ru.monyamau.task_tracker_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.UserApi;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.UserService;

@RestController
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Void> register(UserRequestDto requestDto) {
        TokenResponseDto responseDto = userService.registerUser(requestDto);
        return ResponseEntity.ok()
                .header("Authorization", responseDto.token())
                .build();
    }

    @Override
    public ResponseEntity<UserResponseDto> show(UserPrincipal userPrincipal) {
        return ResponseEntity.ok(new UserResponseDto(userPrincipal.id(), userPrincipal.email()));
    }
}