package ru.monyamau.task_tracker_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.BaseTestContext;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.UserAlreadyExistsException;
import ru.monyamau.task_tracker_backend.repository.UserRepository;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

public class UserServiceTest extends BaseTestContext {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Test
    void shouldRegisterUserSuccessfully() {
        String email = "test@gmail.com";
        String password = "1234567";
        TokenResponseDto responseDto = userService.registerUser(new UserRequestDto(email, password));
        Assertions.assertTrue(userRepository.existsUserByEmail(email));
        User user = userRepository.findUserByEmail(email).get();
        Assertions.assertTrue(passwordEncoder.matches(password, user.getPassword()));
        UserPrincipal principal = jwtTokenProvider.authenticateWithToken(responseDto.token().substring(7)).get();
        Assertions.assertEquals(email, principal.getUsername());
    }

    @Test
    @Transactional
    void shouldThrowUserAlreadyExistsException() {
        String email = "test@gmail.com";
        String password = "1234567";
        userService.registerUser(new UserRequestDto(email, password));
        Assertions.assertTrue(userRepository.existsUserByEmail(email));
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(new UserRequestDto(email, password)));
    }
}