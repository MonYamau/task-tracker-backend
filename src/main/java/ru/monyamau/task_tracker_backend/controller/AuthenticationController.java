package ru.monyamau.task_tracker_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.AuthenticationApi;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.service.AuthenticationService;

@RestController
public class AuthenticationController implements AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<Void> logIn(UserRequestDto requestDto) {
        TokenResponseDto responseDto = authenticationService.authenticateUser(requestDto);
        return ResponseEntity.ok()
                .header("Authorization", responseDto.token())
                .build();
    }
}