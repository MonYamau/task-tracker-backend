package ru.monyamau.task_tracker_backend.util;

import lombok.experimental.UtilityClass;
import ru.monyamau.task_tracker_backend.dto.response.LetterMessageDto;

@UtilityClass
public final class LetterCreator {
    private static final String DEFAULT_TITLE = """
            Успешная регистрация!""";
    private static final String DEFAULT_TEXT = """
            Спасибо за регистрацию в "Task Tracker"! Приятного использования и успехов в поставленных задачах!""";

    public static LetterMessageDto createRegistrationLetter(String recipient) {
        return new LetterMessageDto(recipient,
                DEFAULT_TITLE,
                DEFAULT_TEXT);
    }
}