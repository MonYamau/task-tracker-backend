package ru.monyamau.task_tracker_backend.dto.response;

public record TaskInternalResponseDto(String title, String text, boolean isReady, String ownerEmail) {
}