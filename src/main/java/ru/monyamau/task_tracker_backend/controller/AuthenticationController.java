package ru.monyamau.task_tracker_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.AuthenticationApi;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.service.AuthenticationService;

@RestController
public class AuthenticationController implements AuthenticationApi {
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationService = authenticationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<Void> logIn(UserRequestDto requestDto) {
        UserResponseDto responseDto = authenticationService.authenticateUser(requestDto);
        String token = jwtTokenProvider.createFormattedToken(responseDto.id(), responseDto.username(), responseDto.email());
        return ResponseEntity.ok()
                .header("Authorization", token)
                .build();
    }
}