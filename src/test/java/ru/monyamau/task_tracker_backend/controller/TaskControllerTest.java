package ru.monyamau.task_tracker_backend.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.monyamau.task_tracker_backend.config.SecurityConfig;
import ru.monyamau.task_tracker_backend.dto.request.TaskFormRequestDto;
import ru.monyamau.task_tracker_backend.dto.request.TaskRefRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;
import ru.monyamau.task_tracker_backend.exception.TaskNotFoundException;
import ru.monyamau.task_tracker_backend.filter.JwtFilter;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.TaskService;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class TaskControllerTest {
    private static final String EXAMPLE_EMAIL = "example@gmail.com";
    private static final String EXAMPLE_PASSWORD = "Password123";
    @MockitoBean
    JwtTokenProvider tokenProvider;
    @MockitoBean
    private TaskService taskService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldShowTaskSuccessfully() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskResponseDto responseDto = new TaskResponseDto(1, "test", "test", false, null);

        Mockito.when(taskService.findTask(userPrincipal.id(), new TaskRefRequestDto(1)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/task/{id}", 1)
                        .with(user(userPrincipal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(responseDto.id()))
                .andExpect(jsonPath("title").value(responseDto.title()))
                .andExpect(jsonPath("text").value(responseDto.text()));
    }

    @Test
    void shouldReturn400InvalidInputWithIncorrectIdForFindMethod() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        mockMvc.perform(get("/task/{id}", "X")
                        .with(user(userPrincipal)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists());

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn400InvalidInputWithNullNumberIdForFindMethod() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        mockMvc.perform(get("/task/{id}", 0)
                        .with(user(userPrincipal)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Идентификатор должен быть натуральным числом"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn401UnauthorizedWithoutAuthenticationForFindMethod() throws Exception {
        mockMvc.perform(get("/task/{id}", 1))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("Для доступа к этому ресурсу требуется полная аутентификация"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn404NotFoundWithNullId() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        mockMvc.perform(get("/task/")
                        .with(user(userPrincipal)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);
        TaskResponseDto responseDto = new TaskResponseDto(1, "test", "test", false, null);

        Mockito.when(taskService.saveTask(userPrincipal.id(), requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post("/task")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(responseDto.id()))
                .andExpect(jsonPath("title").value(responseDto.title()))
                .andExpect(jsonPath("text").value(responseDto.text()))
                .andExpect(jsonPath("isReady").value(responseDto.isReady()));
    }

    @Test
    void shouldReturn400InvalidInputWithIncorrectTaskForm() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskFormRequestDto requestDto = new TaskFormRequestDto(null, "test", false);

        mockMvc.perform(post("/task")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Заголовок не может отсутствовать"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn401UnauthorizedWithoutAuthenticationForCreateMethod() throws Exception {
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Для доступа к этому ресурсу требуется полная аутентификация"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn401UnauthorizedWithIncorrectToken() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);

        Mockito.when(taskService.saveTask(userPrincipal.id(), requestDto))
                .thenThrow(new CustomAuthenticationException("не удалось найти пользователя"));

        mockMvc.perform(post("/task")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("не удалось найти пользователя"));
    }

    @Test
    void shouldChangeTaskSuccessfully() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);
        TaskRefRequestDto refRequestDto = new TaskRefRequestDto(1);
        TaskResponseDto responseDto = new TaskResponseDto(1, "test", "test", false, null);

        Mockito.when(taskService.updateTask(userPrincipal.id(), refRequestDto, requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/task/{id}", 1)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(responseDto.id()))
                .andExpect(jsonPath("title").value(responseDto.title()))
                .andExpect(jsonPath("text").value(responseDto.text()))
                .andExpect(jsonPath("isReady").value(responseDto.isReady()));
    }

    @Test
    void shouldReturn400InvalidInputWithNullNumberIdForChangeMethod() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);

        mockMvc.perform(patch("/task/{id}", 0)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Идентификатор должен быть натуральным числом"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn401UnauthorizedWithoutAuthenticationForChangeMethod() throws Exception {
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);

        mockMvc.perform(patch("/task/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Для доступа к этому ресурсу требуется полная аутентификация"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn404NotFoundWithNonexistentTask() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskFormRequestDto requestDto = new TaskFormRequestDto("test", "test", false);
        TaskRefRequestDto refRequestDto = new TaskRefRequestDto(1);

        Mockito.when(taskService.updateTask(userPrincipal.id(), refRequestDto, requestDto))
                .thenThrow(new TaskNotFoundException("не удалось найти задачу"));

        mockMvc.perform(patch("/task/{id}", 1)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("не удалось найти задачу"));
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        mockMvc.perform(delete("/task/{id}", 1)
                        .with(user(userPrincipal)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        Mockito.verify(taskService, Mockito.times(1))
                .deleteTask(userPrincipal.id(), new TaskRefRequestDto(1));
    }

    @Test
    void shouldReturn400InvalidInputWithNegativeNumberIdForDeleteMethod() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        mockMvc.perform(delete("/task/{id}", -1)
                        .with(user(userPrincipal)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Идентификатор должен быть натуральным числом"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn401UnauthorizedWithoutAuthenticationForDeleteMethod() throws Exception {
        mockMvc.perform(delete("/task/{id}", 1))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Для доступа к этому ресурсу требуется полная аутентификация"));

        Mockito.verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturn404NotFoundWithIncorrectId() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        Mockito.doThrow(new TaskNotFoundException("не удалось найти задачу"))
                .when(taskService).deleteTask(userPrincipal.id(), new TaskRefRequestDto(10));

        mockMvc.perform(delete("/task/{id}", 10)
                        .with(user(userPrincipal)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("не удалось найти задачу"));
    }
}