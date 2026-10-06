package ru.monyamau.task_tracker_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.BaseTestContext;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.AuthenticationException;
import ru.monyamau.task_tracker_backend.mapper.TaskMapper;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

import java.util.List;

public class TaskCollectionServiceTest extends BaseTestContext {
    @Autowired
    private TaskCollectionService taskCollectionService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskMapper taskMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("test@gmail.com", "1234567"));
    }

    @Transactional
    @Test
    void shouldFindTasksOfUser() {
        Task saved1 = taskRepository.save(new Task("testTitle1", "testTest", false, user, null));
        Task saved2 = taskRepository.save(new Task("testTitle2", "testTest", false, user, null));
        List<TaskResponseDto> responseDtoList = taskCollectionService.findAll(user.getId());
        Assertions.assertFalse(responseDtoList.isEmpty());
        Assertions.assertTrue(responseDtoList.contains(taskMapper.toDto(saved1)));
        Assertions.assertTrue(responseDtoList.contains(taskMapper.toDto(saved2)));
    }

    @Test
    @Transactional
    void shouldThrowAuthenticationException() {
        Assertions.assertThrows(AuthenticationException.class, () -> taskCollectionService.findAll(2));
    }
}