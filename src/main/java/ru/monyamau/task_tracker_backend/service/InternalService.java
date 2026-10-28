package ru.monyamau.task_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.request.PeriodTimeRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskInternalResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;

import java.util.List;

@Service
public class InternalService {
    private final TaskRepository taskRepository;

    public InternalService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskInternalResponseDto> findAll(PeriodTimeRequestDto requestDto) {
        List<Task> tasks = taskRepository.findAllForReport(requestDto.from(), requestDto.to());
        return tasks.stream().map(this::convert).toList();
    }

    private TaskInternalResponseDto convert(Task task) {
        return new TaskInternalResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(),
                task.getPlannedCompletionAt(), task.getCompletedAt(), task.getOwner().getId());
    }
}