package ru.monyamau.task_tracker_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Данные пользователя для аутентификации")
public record UserRequestDto(
        @Schema(description = "Электронная почта пользователя", example = "example@gmail.com")
        @NotBlank(message = "Электронная почта не может отсутствовать")
        @Email(message = "Невалидный формат электронной почты")
        String email,
        @Schema(description = "Пароль пользователя", example = "QWERTY123!")
        @NotBlank(message = "Пароль не может отсутствовать")
        @Pattern(regexp = "^[A-Za-z0-9_.,+=\\\\—~?!@#$%^&;'-]+$",
                message = "Пароль может содержать только латинские буквы, цифры и некоторые спецсимволы")
        String password) {
    public UserRequestDto {
        if (email != null) {
            email = email.trim().toLowerCase();
        }
    }
}