package ru.monyamau.task_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.TaskNotFoundException;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

@Service
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public TaskResponseDto findTask(Integer userId, TaskRefRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(requestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(), task.getCompletedAt());
    }

    @Transactional
    public TaskResponseDto saveTask(Integer userId, TaskFormRequestDto requestDto) {
        User user = userRepository.getReferenceById(userId);
        Task savedTask = taskRepository.saveAndFlush(new Task(requestDto.title(), requestDto.text(), false, user, null));
        return new TaskResponseDto(savedTask.getId(), savedTask.getTitle(), savedTask.getText(), savedTask.isReady(),
                savedTask.getCompletedAt());
    }

    @Transactional
    public TaskResponseDto updateTask(Integer userId, TaskRefRequestDto refRequestDto, TaskFormRequestDto formRequestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(refRequestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        task.update(formRequestDto.title(), formRequestDto.text(),
                formRequestDto.isReady(), formRequestDto.completedAt());
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(), task.getCompletedAt());
    }

    @Transactional
    public void deleteTask(Integer userId, TaskRefRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(requestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        taskRepository.delete(task);
    }
}