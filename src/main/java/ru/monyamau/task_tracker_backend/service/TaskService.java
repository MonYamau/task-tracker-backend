package ru.monyamau.task_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.TaskNotFoundException;
import ru.monyamau.task_tracker_backend.mapper.TaskMapper;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

@Service
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository, TaskMapper taskMapper) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public TaskResponseDto findTask(Integer userId, TaskRefRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(requestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        return taskMapper.toDto(task);
    }

    @Transactional
    public TaskResponseDto saveTask(Integer userId, TaskFormRequestDto requestDto) {
        User user = userRepository.getReferenceById(userId);
        Task savedTask = taskRepository.saveAndFlush(new Task(requestDto.title(), requestDto.text(), false, user, null));
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(Integer userId, TaskRefRequestDto refRequestDto, TaskFormRequestDto formRequestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(refRequestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        task.update(formRequestDto.title(), formRequestDto.text(),
                formRequestDto.isReady(), formRequestDto.completedAt());
        return taskMapper.toDto(task);
    }

    @Transactional
    public void deleteTask(Integer userId, TaskRefRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(requestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        taskRepository.delete(task);
    }
}