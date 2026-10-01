package ru.monyamau.task_tracker_backend.dto.request;

import java.time.OffsetDateTime;

public record TaskFormRequestDto(String title, String text, boolean isReady, OffsetDateTime completedAt) {
}
