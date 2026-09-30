package ru.monyamau.task_tracker_backend.dto.request;

public record UserRequestDto(String username, String password, String email) {
}