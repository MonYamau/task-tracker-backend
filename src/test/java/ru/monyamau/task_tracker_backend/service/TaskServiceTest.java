package ru.monyamau.task_tracker_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.monyamau.task_tracker_backend.BaseTestContext;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.exception.AuthenticationException;
import ru.monyamau.task_tracker_backend.exception.TaskNotFoundException;
import ru.monyamau.task_tracker_backend.repository.TaskRepository;
import ru.monyamau.task_tracker_backend.repository.UserRepository;

@Transactional
public class TaskServiceTest extends BaseTestContext {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    private Integer firstUserId;
    private Integer secondUserId;
    private Task testTaskOfFirstUser;
    private Task notSavedTestTask;

    @BeforeEach
    void setUp() {
        User firstUser = userRepository.save(new User("test@gmail.com", "1234567"));
        User secondUser = userRepository.save(new User("test2@gmail.com", "1234567"));
        firstUserId = firstUser.getId();
        secondUserId = secondUser.getId();
        testTaskOfFirstUser = taskRepository.save(new Task("testTitle", "testText", false, firstUser, null));
        notSavedTestTask = new Task("test", "test", false, firstUser, null);
    }

    @Test
    void shouldSavePersonalTaskSuccessfully() {
        TaskResponseDto responseDto = taskService.saveTask(firstUserId,
                new TaskFormRequestDto(notSavedTestTask.getTitle(), notSavedTestTask.getText(), notSavedTestTask.isReady()));
        Assertions.assertNotNull(responseDto);
        Assertions.assertTrue(taskRepository.existsById(responseDto.id()));
        Assertions.assertTrue(taskRepository.findTaskByIdAndOwnerId(responseDto.id(), firstUserId).isPresent());
        Assertions.assertEquals(responseDto, new TaskResponseDto(responseDto.id(), notSavedTestTask.getTitle(),
                notSavedTestTask.getText(), notSavedTestTask.isReady(), notSavedTestTask.getCompletedAt()));
    }

    @Test
    void shouldThrowAuthenticationExceptionForSaveMethod() {
        Integer incorrectId = 10;
        Assertions.assertThrows(AuthenticationException.class, () -> taskService.saveTask(incorrectId,
                new TaskFormRequestDto(notSavedTestTask.getTitle(), notSavedTestTask.getText(), notSavedTestTask.isReady())));
    }

    @Test
    void shouldFindPersonalTaskSuccessfully() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        TaskResponseDto dto = taskService.findTask(firstUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId()));
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto, new TaskResponseDto(testTaskOfFirstUser.getId(), testTaskOfFirstUser.getTitle(),
                testTaskOfFirstUser.getText(), testTaskOfFirstUser.isReady(), testTaskOfFirstUser.getCompletedAt()));
    }

    @Test
    void shouldThrowTaskNotFoundExceptionForFindMethod() {
        Integer incorrectId = 10;
        Assertions.assertFalse(taskRepository.existsById(incorrectId));
        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.findTask(firstUserId, new TaskRefRequestDto(incorrectId)));
    }

    @Test
    void shouldNotFindPersonalTaskOfOtherUser() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        TaskResponseDto dto = taskService.findTask(firstUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId()));
        Assertions.assertEquals(dto.id(), testTaskOfFirstUser.getId());
        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.findTask(secondUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId())));
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        TaskResponseDto responseDto = taskService.updateTask(firstUserId,
                new TaskRefRequestDto(testTaskOfFirstUser.getId()), new TaskFormRequestDto("test", "test", false));
        Assertions.assertEquals(responseDto.id(), testTaskOfFirstUser.getId());
        Assertions.assertEquals(responseDto, new TaskResponseDto(responseDto.id(), "test", "test", false, null));
    }

    @Test
    void shouldUpdateCompletedDateTimeSuccessfully() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        Assertions.assertNull(testTaskOfFirstUser.getCompletedAt());
        TaskResponseDto responseDto = taskService.updateTask(firstUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId()),
                new TaskFormRequestDto(testTaskOfFirstUser.getTitle(), testTaskOfFirstUser.getText(), true));
        Assertions.assertNotNull(testTaskOfFirstUser.getCompletedAt());
        Assertions.assertNotNull(responseDto.completedAt());
    }

    @Test
    void shouldThrowTaskNotFoundExceptionForUpdateMethod() {
        Integer incorrectTaskId = 10;
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(firstUserId,
                new TaskRefRequestDto(incorrectTaskId), new TaskFormRequestDto("test", "test", false)));
    }

    @Test
    void shouldNotUpdatePersonalTaskOfOtherUser() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        TaskResponseDto dto = taskService.updateTask(firstUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId()),
                new TaskFormRequestDto("test", "test", false));
        Assertions.assertEquals(dto.id(), testTaskOfFirstUser.getId());
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(secondUserId,
                new TaskRefRequestDto(testTaskOfFirstUser.getId()), new TaskFormRequestDto("test1", "test2", false)));
        Assertions.assertEquals("test", testTaskOfFirstUser.getTitle());
        Assertions.assertEquals("test", testTaskOfFirstUser.getText());
    }

    @Test
    void shouldDeletePersonalTaskSuccessfully() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        taskService.deleteTask(firstUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId()));
        Assertions.assertFalse(taskRepository.existsById(testTaskOfFirstUser.getId()));
    }

    @Test
    void shouldNotDeletePersonalTaskOfOtherUser() {
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.deleteTask(secondUserId, new TaskRefRequestDto(testTaskOfFirstUser.getId())));
        Assertions.assertTrue(taskRepository.existsById(testTaskOfFirstUser.getId()));
    }

    @Test
    void shouldThrowTaskNotFoundExceptionForDeleteMethod() {
        Integer incorrectTaskId = 10;
        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.deleteTask(firstUserId, new TaskRefRequestDto(incorrectTaskId)));
    }
}