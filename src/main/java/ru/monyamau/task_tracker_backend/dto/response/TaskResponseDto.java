package ru.monyamau.task_tracker_backend.dto.response;

import java.time.OffsetDateTime;

public record TaskResponseDto(Integer id, String title, String text, boolean isReady, OffsetDateTime completedAt) {
}
