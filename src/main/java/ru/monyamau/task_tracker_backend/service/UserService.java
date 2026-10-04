package ru.monyamau.task_tracker_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.UserAlreadyExistsException;
import ru.monyamau.task_tracker_backend.repository.UserRepository;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public TokenResponseDto registerUser(UserRequestDto requestDto) {
        checkEmailForUniqueness(requestDto.email());
        String hashedPassword = passwordEncoder.encode(requestDto.password());
        User savedUser;
        try {
            savedUser = userRepository.saveAndFlush(new User(requestDto.email(), hashedPassword));
            log.info("Успешное создание пользователя c ID: {}", savedUser.getId());
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException constraintException) {
                validateConstraintException(constraintException.getConstraintName(), requestDto.email());
            }
            throw new IllegalStateException("Не удалось зарегистрировать пользователя", e);
        }
        String token = jwtTokenProvider.createFormattedToken(savedUser.getId(), savedUser.getEmail());
        log.info("Успешная регистрация и создание токена для пользователя с ID: {}", savedUser.getId());
        return new TokenResponseDto(token);
    }

    private void checkEmailForUniqueness(String email) {
        if (userRepository.existsUserByEmail(email)) {
            log.warn("Не удалось создать пользователя из-за конфликта электронной почты: {}", email);
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }

    private void validateConstraintException(String constraintName, String email) {
        if (constraintName != null && constraintName.contains("email")) {
            log.warn("Не удалось создать пользователя из-за конфликта электронной почты: {}", email);
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }
}