package ru.monyamau.task_tracker_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Данные персональной задачи")
public record TaskRefRequestDto(
        @Schema(description = "Числовой идентификатор персональной задачи", example = "123")
        @NotNull(message = "Идентификатор не может отсутствовать")
        @Positive(message = "ИЫдентификатор должен быть натуральным числом")
        Integer id) {
}