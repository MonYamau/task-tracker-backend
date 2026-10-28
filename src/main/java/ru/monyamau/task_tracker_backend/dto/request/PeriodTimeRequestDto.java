package ru.monyamau.task_tracker_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.OffsetDateTime;

public record PeriodTimeRequestDto(
        @NotNull(message = "Поле начала периода не может отсутствовать")
        @PastOrPresent(message = "Некорректное значение: начало периода не может быть в будущем")
        OffsetDateTime from,
        @NotNull(message = "Поле окончания периода не может отсутствовать")
        @PastOrPresent(message = "Некорректное значение: окончание периода не может быть в будущем")
        OffsetDateTime to) {
}