package ru.monyamau.task_tracker_backend.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto registerUser(UserRequestDto requestDto) {
        validateUniqueFields(requestDto.username(), requestDto.email());
        User savedUser;
        try {
            savedUser = userRepository.saveAndFlush(new User(requestDto.username(), requestDto.password(), requestDto.email()));
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException constraintException) {
                String constraintName = constraintException.getConstraintName();
                if (constraintName != null && constraintName.contains("username")) {
                    throw new UserAlreadyExistsException("Пользователь с именем " + requestDto.username() + " уже существует");
                }
                if (constraintName != null && constraintName.contains("email")) {
                    throw new UserAlreadyExistsException("Пользователь с почтой " + requestDto.email() + " уже существует");
                }
            }
            throw new UserAlreadyExistsException("Текущий пользователь уже существует");
        }
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    private void validateUniqueFields(String username, String email) {
        if (userRepository.existsUserByUsername(username)) {
            throw new UserAlreadyExistsException("Пользователь с именем " + username + " уже существует");
        }
        if (userRepository.existsUserByEmail(email)) {
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }
}