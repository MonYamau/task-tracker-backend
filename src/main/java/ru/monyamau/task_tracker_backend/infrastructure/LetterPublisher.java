package ru.monyamau.task_tracker_backend.infrastructure;

import ru.monyamau.task_tracker_backend.dto.response.LetterMessageDto;

public interface LetterPublisher {
    void publishLetterTask(LetterMessageDto messageDto);
}