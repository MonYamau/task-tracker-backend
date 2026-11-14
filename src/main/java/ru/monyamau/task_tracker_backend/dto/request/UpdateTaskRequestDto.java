package ru.monyamau.task_tracker_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

@Schema(description = "Данные персональной задачи для её редактирования")
public record UpdateTaskRequestDto(
        @Schema(description = "Заголовок персональной задачи", example = "Сделать покупки")
        @NotBlank(message = "Заголовок не может отсутствовать")
        @Size(max = 100, message = "Заголовок не может превышать 100 символов")
        String title,
        @Schema(description = "Описание персональной задачи", example = "Купить молоко, хлеб, дом")
        @Size(max = 300, message = "Описание не может превышать 300 символов")
        String text,
        @Schema(description = "Планируемое время выполнения задачи", example = "2026-10-10T12:00:00Z")
        OffsetDateTime plannedCompletionAt,
        @Schema(description = "Статус готовности персональной задачи")
        Boolean isReady) {
}
