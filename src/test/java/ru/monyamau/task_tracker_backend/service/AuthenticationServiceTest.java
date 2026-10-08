package ru.monyamau.task_tracker_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.BaseTestContext;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

public class AuthenticationServiceTest extends BaseTestContext {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        password = "1234567";
        userService.registerUser(new UserRequestDto(email, password));
    }

    @Test
    @Transactional
    void shouldAuthenticateUserSuccessfully() {
        TokenResponseDto responseDto = authenticationService.authenticateUser(new UserRequestDto(email, password));
        Assertions.assertFalse(responseDto.token().isBlank());
        UserPrincipal principal = jwtTokenProvider.authenticateWithToken(responseDto.token().substring(7)).get();
        Assertions.assertEquals(email, principal.getUsername());
    }

    @Test
    @Transactional
    void shouldThrowAuthenticationException() {
        String incorrectPassword = "12345678!";
        Assertions.assertThrows(CustomAuthenticationException.class,
                () -> authenticationService.authenticateUser(new UserRequestDto(email, incorrectPassword)));
    }
}