package ru.monyamau.task_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.AuthenticationException;
import ru.monyamau.task_tracker_backend.mapper.TaskMapper;
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
    private final TaskMapper taskMapper;

    public TaskCollectionService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskResponseDto> findAll(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new AuthenticationException("Не удалось найти пользователя по текущему токену");
        }
        User user = userRepository.getReferenceById(userId);
        List<Task> tasks = taskRepository.findAllByOwner(user);
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}