package ru.monyamau.task_tracker_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;
import ru.monyamau.task_tracker_backend.mapper.TaskMapper;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class TaskCollectionService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskCollectionService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll(Integer userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с ID {} прошёл аутентификацию фильтра, но отсутствует в базе данных", userId);
            throw new CustomAuthenticationException("Не удалось найти пользователя по текущему токену");
        }
        List<Task> tasks = taskRepository.findAllByOwnerId(userId);
        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }
}