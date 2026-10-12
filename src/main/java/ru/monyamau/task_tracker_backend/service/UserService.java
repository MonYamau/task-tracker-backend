package ru.monyamau.task_tracker_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.LetterMessageDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.LetterPublisherException;
import ru.monyamau.task_tracker_backend.exception.UserAlreadyExistsException;
import ru.monyamau.task_tracker_backend.infrastructure.LetterPublisher;
import ru.monyamau.task_tracker_backend.repository.UserRepository;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.util.LetterCreator;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TransactionTemplate transactionTemplate;
    private final LetterPublisher letterPublisher;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, TransactionTemplate transactionTemplate, LetterPublisher letterPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.transactionTemplate = transactionTemplate;
        this.letterPublisher = letterPublisher;
    }

    public TokenResponseDto registerUser(UserRequestDto requestDto) {
        String hashedPassword = passwordEncoder.encode(requestDto.password());
        User savedUser = saveUser(requestDto.email(), hashedPassword);
        LetterMessageDto messageDto = LetterCreator.createLetter(requestDto.email());
        try {
            letterPublisher.sendLetter(messageDto);
        } catch (LetterPublisherException e) {
            log.error("Не удалось подтвердить отправку письма для зарегистрированного пользователя с ID {}",
                    savedUser.getId(), e);
        }
        String token = jwtTokenProvider.createFormattedToken(savedUser.getId(), savedUser.getEmail());
        log.info("Успешная регистрация для пользователя с ID: {}", savedUser.getId());
        return new TokenResponseDto(token);
    }

    private User saveUser(String email, String hashedPassword) {
        return transactionTemplate.execute(status -> {
            if (userRepository.existsUserByEmail(email)) {
                log.warn("Не удалось создать пользователя из-за конфликта электронной почты: {}", email);
                throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
            }
            try {
                User savedUser = userRepository.saveAndFlush(new User(email, hashedPassword));
                log.info("Успешное создание пользователя c ID: {}", savedUser.getId());
                return savedUser;
            } catch (DataIntegrityViolationException e) {
                if (e.getCause() instanceof ConstraintViolationException constraintException) {
                    validateConstraintException(constraintException.getConstraintName(), email);
                }
                throw new IllegalStateException("Не удалось зарегистрировать пользователя", e);
            }
        });
    }

    private void validateConstraintException(String constraintName, String email) {
        if (constraintName != null && constraintName.contains("email")) {
            log.warn("Не удалось создать пользователя из-за конфликта электронной почты: {}", email);
            throw new UserAlreadyExistsException("Пользователь с почтой " + email + " уже существует");
        }
    }
}