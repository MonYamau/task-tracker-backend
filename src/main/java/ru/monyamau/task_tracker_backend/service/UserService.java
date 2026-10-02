package ru.monyamau.task_tracker_backend.service;

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
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException constraintException) {
                validateConstraintException(constraintException.getConstraintName(), requestDto.email());
            }
            throw new UserAlreadyExistsException("Текущий пользователь уже существует");
        }
        String token = jwtTokenProvider.createFormattedToken(savedUser.getId(), savedUser.getEmail());
        return new TokenResponseDto(token);
    }

    private void checkEmailForUniqueness(String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }

    private void validateConstraintException(String constraintName, String email) {
        if (constraintName != null && constraintName.contains("email")) {
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }
}