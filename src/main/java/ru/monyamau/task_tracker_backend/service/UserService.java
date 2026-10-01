package ru.monyamau.task_tracker_backend.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.UserAlreadyExistsException;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto registerUser(UserRequestDto requestDto) {
        if (userRepository.existsUserByEmail(requestDto.email())) {
            throw new UserAlreadyExistsException("Пользователь с почтой " + requestDto.email() + " уже существует");
        }
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
        return new UserResponseDto(savedUser.getId(), savedUser.getEmail());
    }

    private void validateConstraintException(String constraintName, String email) {
        if (constraintName != null && constraintName.contains("email")) {
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }
}