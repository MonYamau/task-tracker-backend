package ru.monyamau.task_tracker_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с данными о возникшей ошибке")
public record ErrorDto(
        @Schema(description = "Сообщение о возникшей ошибке", example = "Возникла неизвестная ошибка")
        String message) {
}