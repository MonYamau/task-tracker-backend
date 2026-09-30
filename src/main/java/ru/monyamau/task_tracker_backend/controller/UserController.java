package ru.monyamau.task_tracker_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.UserApi;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.UserService;

@RestController
public class UserController implements UserApi {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<Void> register(UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.registerUser(requestDto);
        String token = jwtTokenProvider.createFormattedToken(responseDto.id(), responseDto.username(), responseDto.email());
        return ResponseEntity.ok()
                .header("Authorization", token)
                .build();
    }

    @Override
    public ResponseEntity<UserResponseDto> show(UserPrincipal userPrincipal) {
        return ResponseEntity.ok(new UserResponseDto(userPrincipal.id(), userPrincipal.username(), userPrincipal.email()));
    }
}