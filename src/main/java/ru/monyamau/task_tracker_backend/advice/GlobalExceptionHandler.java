package ru.monyamau.task_tracker_backend.advice;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.monyamau.task_tracker_backend.dto.response.ErrorDto;
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;
import ru.monyamau.task_tracker_backend.exception.InvalidInputException;
import ru.monyamau.task_tracker_backend.exception.TaskNotFoundException;
import ru.monyamau.task_tracker_backend.exception.UserAlreadyExistsException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDto errorDto = new ErrorDto("Validation error");
        if (ex.getBindingResult().getFieldError() != null) {
            errorDto = new ErrorDto(ex.getBindingResult().getFieldError().getDefaultMessage());
        }
        log.warn("Validation error: {}", errorDto.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDto> handleInvalidInputException(Exception e) {
        log.warn("Ошибка некорректного ввода пользователя: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler({CustomAuthenticationException.class,
            AuthenticationException.class})
    public ResponseEntity<ErrorDto> handleAuthenticationException(Exception e) {
        String message = e.getMessage();
        if (e instanceof AuthenticationException) {
            message = "Для доступа к этому ресурсу требуется полная аутентификация";
        }
        log.warn("Ошибка аутентификации пользователя: {}", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(message));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception e) {
        log.warn("Ошибка отсутствия ресурса: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleAlreadyExistsException(Exception e) {
        log.warn("Ошибка конфликта ресурсов: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnexpectedException(Exception e) {
        log.error("Непредвиденная ошибка: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Возникла ошибка на стороне сервера"));
    }
}