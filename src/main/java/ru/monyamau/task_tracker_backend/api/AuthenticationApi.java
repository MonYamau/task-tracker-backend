package ru.monyamau.task_tracker_backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;

@RequestMapping("/auth")
public interface AuthenticationApi {
    @PostMapping("/login")
    ResponseEntity<Void> logIn(@RequestBody UserRequestDto requestDto);
}
