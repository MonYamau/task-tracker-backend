package ru.monyamau.task_tracker_backend.dto.request;

public record TaskModificationRequestDto(TaskRequestDto before, TaskRequestDto after) {
}
