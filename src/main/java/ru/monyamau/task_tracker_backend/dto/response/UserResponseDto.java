package ru.monyamau.task_tracker_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с данными о пользователе")
public record UserResponseDto(
        @Schema(description = "Числовой идентификатор пользователя", example = "1")
        Integer id,
        @Schema(description = "Электронная почта пользователя", example = "example@gmail.com")
        String email) {
}