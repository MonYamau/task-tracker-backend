package ru.monyamau.task_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskCollectionService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskCollectionService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponseDto> findAll(Integer userId) {
        User currentUser = userRepository.findUserById(userId).orElseThrow(() ->
                new IllegalStateException("Не удалось найти актуального пользователя с ID: " + userId));
        List<Task> tasks = taskRepository.findAllByOwner(currentUser);
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return tasks.stream()
                .map(task -> new TaskResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(), task.getCompletedAt()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}