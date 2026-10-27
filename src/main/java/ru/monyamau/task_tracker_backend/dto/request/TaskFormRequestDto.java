package ru.monyamau.task_tracker_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

@Schema(description = "Данные персональной задачи для её создания/редактирования")
public record TaskFormRequestDto(
        @Schema(description = "Заголовок персональной задачи", example = "Сделать покупки")
        @NotBlank(message = "Заголовок не может отсутствовать")
        @Size(max = 255, message = "Заголовок не может превышать 255 символов")
        String title,
        @Schema(description = "Описание персональной задачи", example = "Купить молоко, хлеб, дом")
        @Size(max = 255, message = "Описание не может превышать 255 символов")
        String text,
        @Schema(description = "Планируемое время выполнения задачи", example = "2026-10-10T12:00:00Z")
        @FutureOrPresent(message = "Нельзя запланировать выполнение задачи на прошлое")
        OffsetDateTime plannedCompletionAt,
        @Schema(description = "Статус готовности персональной задачи")
        boolean isReady) {
}