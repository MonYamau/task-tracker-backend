package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.ErrorDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

@RequestMapping("/task")
@Tag(name = "Персональная задача", description = "Управление персональной задачей пользователя")
public interface TaskApi {
    @GetMapping("/{id}")
    @Operation(summary = "Найти персональную задачу пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос на получение информации о задаче")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных параметров",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "401", description = "Пользователь неавторизован",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Не удалось найти персональную задачу",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    ResponseEntity<TaskResponseDto> show(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                         @Valid @ModelAttribute(name = "id") TaskRefRequestDto requestDto);

    @PostMapping
    @Operation(summary = "Создать персональную задачу пользователя")
    @ApiResponse(responseCode = "201", description = "Успешный запрос на создание задачи")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных параметров",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "401", description = "Пользователь неавторизован",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    ResponseEntity<TaskResponseDto> create(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                           @Valid @RequestBody TaskFormRequestDto requestDto);

    @PatchMapping("/{id}")
    @Operation(summary = "Изменить персональную задачу пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос на изменение задачи")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных параметров",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "401", description = "Пользователь неавторизован",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Не удалось найти персональную задачу",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    ResponseEntity<TaskResponseDto> edit(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                         @Valid @ModelAttribute(name = "id") TaskRefRequestDto refRequestDto,
                                         @Valid @RequestBody TaskFormRequestDto formRequestDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить персональную задачу пользователя")
    @ApiResponse(responseCode = "204", description = "Успешный запрос на удаление задачи")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных параметров",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "401", description = "Пользователь неавторизован",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Не удалось найти персональную задачу",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    ResponseEntity<Void> delete(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal,
                                @Valid @ModelAttribute(name = "id") TaskRefRequestDto requestDto);
}
