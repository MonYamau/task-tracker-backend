package ru.monyamau.task_tracker_backend.dto.response;

import java.time.OffsetDateTime;

public record TaskInternalResponseDto(Integer id, String title, String text, boolean isReady,
                                      OffsetDateTime plannedCompletionAt, OffsetDateTime completedAt, Integer ownerId,
                                      String ownerEmail) {
}