package ru.monyamau.task_tracker_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.mapper.TaskMapper;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;

import java.util.List;

@Slf4j
@Service
public class TaskCollectionService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskCollectionService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll(Integer userId) {
        List<Task> tasks = taskRepository.findAllByOwnerId(userId);
        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }
}