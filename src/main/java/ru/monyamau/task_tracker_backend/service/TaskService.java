package ru.monyamau.task_tracker_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public TaskResponseDto findTask(Integer userId, TaskRefRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(requestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(), task.getCompletedAt());
    }

    public TaskResponseDto saveTask(Integer userId, TaskFormRequestDto requestDto) {
        User currentUser = userRepository.findUserById(userId).orElseThrow(() ->
                new IllegalStateException("Не удалось найти актуального пользователя с ID: " + userId));
        Task savedTask = taskRepository.saveAndFlush(new Task(requestDto.title(), requestDto.text(), false, currentUser, null));
        return new TaskResponseDto(savedTask.getId(), savedTask.getTitle(), savedTask.getText(), savedTask.isReady(),
                savedTask.getCompletedAt());
    }

    public TaskResponseDto updateTask(Integer userId, TaskRefRequestDto refRequestDto, TaskFormRequestDto formRequestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(refRequestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        task.update(formRequestDto.title(), formRequestDto.text(),
                formRequestDto.isReady(), formRequestDto.completedAt());
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getText(), task.isReady(), task.getCompletedAt());
    }

    public void deleteTask(Integer userId, TaskRefRequestDto requestDto) {
        taskRepository.deleteByIdAndOwnerId(requestDto.id(), userId);
    }
}