package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.ErrorDto;
import ru.monyamau.task_tracker_backend.dto.response.UserResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

@RequestMapping("/user")
@Tag(name = "Пользователь", description = "Управление пользовательской информацией")
public interface UserApi {
    @PostMapping
    @Operation(summary = "Зарегистрировать пользователя")
    @ApiResponse(responseCode = "201", description = "Успешная регистрация")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных параметров",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "409", description = "Пользователь уже существует",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @SecurityRequirements
    ResponseEntity<Void> register(@RequestBody UserRequestDto requestDto);

    @GetMapping
    @Operation(summary = "Получить текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос на получение информации о пользователе")
    @ApiResponse(responseCode = "401", description = "Пользователь неавторизован",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    ResponseEntity<UserResponseDto> show(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal);
}