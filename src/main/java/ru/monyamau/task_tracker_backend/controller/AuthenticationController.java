package ru.monyamau.task_tracker_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.AuthenticationApi;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;

@RestController
public class AuthenticationController implements AuthenticationApi {
    @Override
    public ResponseEntity<Void> logIn(UserRequestDto requestDto) {
        return null;
    }
}
