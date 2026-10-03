package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.monyamau.task_tracker_backend.dto.response.ErrorDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

import java.util.List;

@RequestMapping("/tasks")
@Tag(name = "Список задач", description = "Управление персональным списком задач пользователя")
public interface TaskCollectionApi {
    @GetMapping
    @Operation(summary = "Найти персональный список задач пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный запрос на получение списка задач")
    @ApiResponse(responseCode = "401", description = "Пользователь неавторизован",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    ResponseEntity<List<TaskResponseDto>> show(@AuthenticationPrincipal @Parameter(hidden = true) UserPrincipal userPrincipal);
}