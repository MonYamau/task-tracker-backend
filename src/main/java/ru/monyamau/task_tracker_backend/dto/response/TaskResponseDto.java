package ru.monyamau.task_tracker_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Schema(description = "Ответ с данными о персональной задаче")
public record TaskResponseDto(
        @Schema(description = "Числовой идентификатор персональной задачи", example = "123")
        Integer id,
        @Schema(description = "Заголовок персональной задачи", example = "Сделать покупки")
        String title,
        @Schema(description = "Описание персональной задачи", example = "Купить молоко, хлеб, дом")
        String text,
        @Schema(description = "Статус готовности персональной задачи")
        boolean isReady,
        @JsonInclude(NON_NULL)
        @Schema(description = "Время выполнения персональной задачи")
        OffsetDateTime completedAt) {
}
