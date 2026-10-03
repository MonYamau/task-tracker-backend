package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.ErrorDto;

@RequestMapping("/auth")
@Tag(name = "Авторизация", description = "Управление доступом пользователя")
public interface AuthenticationApi {
    @PostMapping("/login")
    @Operation(summary = "Аутентифицировать пользователя")
    @ApiResponse(responseCode = "200", description = "Успешная аутентификация")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных параметров",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @SecurityRequirements
    ResponseEntity<Void> logIn(@Valid @RequestBody UserRequestDto requestDto);
}
