package ru.monyamau.task_tracker_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Данные персональной задачи")
public record TaskRefRequestDto(
        @Schema(description = "Числовой идентификатор персональной задачи", example = "123")
        @Positive(message = "Идентификатор должен быть натуральным числом")
        Integer id) {
}