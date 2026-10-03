package ru.monyamau.task_tracker_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные персональной задачи для её создания/редактирования")
public record TaskFormRequestDto(
        @Schema(description = "Заголовок персональной задачи", example = "Сделать покупки")
        @NotBlank(message = "Заголовок не может отсутствовать")
        @Size(max = 255, message = "Заголовок не может превышать 255 символов")
        String title,
        @Schema(description = "Описание персональной задачи", example = "Купить молоко, хлеб, дом")
        @Size(max = 255, message = "Описание не может превышать 255 символов")
        String text,
        @Schema(description = "Статус готовности персональной задачи")
        boolean isReady) {
}
