package ru.monyamau.task_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.response.TaskInternalResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.exception.InvalidInputException;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class InternalService {
    private final TaskRepository taskRepository;

    public InternalService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskInternalResponseDto> findAll(OffsetDateTime from, OffsetDateTime to) {
        if (to.isBefore(from)) {
            throw new InvalidInputException("Время окончания не может быть раньше времени начала");
        }
        List<Task> tasks = taskRepository.findAllForReport(from, to);
        return tasks.stream().map(this::convert).toList();
    }

    private TaskInternalResponseDto convert(Task task) {
        return new TaskInternalResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(),
                task.getPlannedCompletionAt(), task.getCompletedAt(), task.getOwner().getId(), task.getOwner().getEmail());
    }
}