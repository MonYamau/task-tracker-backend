package ru.monyamau.task_tracker_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.dto.request.CreateTaskRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.UpdateTaskRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.TaskNotFoundException;
import ru.monyamau.task_tracker_backend.mapper.TaskMapper;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Slf4j
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
    public TaskResponseDto saveTask(Integer userId, CreateTaskRequestDto requestDto) {
        User user = userRepository.getReferenceById(userId);
        OffsetDateTime adjustedTime = adjustPlannedCompletionTimeForNewTask(requestDto.plannedCompletionAt());
        Task savedTask = taskRepository.saveAndFlush(new Task(requestDto.title(), requestDto.text(), false, adjustedTime, user, null));
        log.info("Пользователь с ID {} успешно создал персональную задачу с ID {}", userId, savedTask.getId());
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(Integer userId, TaskRefRequestDto refRequestDto, UpdateTaskRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(refRequestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        boolean completion = !(requestDto.isReady() == null || !requestDto.isReady());
        task.update(requestDto.title(), requestDto.text(), requestDto.plannedCompletionAt(), completion);
        log.info("Пользователь с ID {} успешно изменил персональную задачу с ID {}", userId, task.getId());
        return taskMapper.toDto(task);
    }

    @Transactional
    public void deleteTask(Integer userId, TaskRefRequestDto requestDto) {
        Task task = taskRepository.findTaskByIdAndOwnerId(requestDto.id(), userId).orElseThrow(() ->
                new TaskNotFoundException("Не удалось найти задачу"));
        taskRepository.delete(task);
        log.info("Пользователь с ID {} успешно удалил персональную задачу с ID {}", userId, task.getId());
    }

    private OffsetDateTime adjustPlannedCompletionTimeForNewTask(OffsetDateTime clientTime) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
        if (clientTime == null) {
            return now;
        }
        return clientTime;
    }
}