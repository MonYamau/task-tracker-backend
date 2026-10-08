package ru.monyamau.task_tracker_backend.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.monyamau.task_tracker_backend.config.SecurityConfig;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;
import ru.monyamau.task_tracker_backend.filter.JwtFilter;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.TaskCollectionService;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskCollectionController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class TaskCollectionControllerTest {
    private static final String EXAMPLE_EMAIL = "example@gmail.com";
    private static final String EXAMPLE_PASSWORD = "Password123";
    @MockitoBean
    JwtTokenProvider tokenProvider;
    @MockitoBean
    private TaskCollectionService taskCollectionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldShowTasksSuccessfully() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        TaskResponseDto responseDto = new TaskResponseDto(1, "test", "test", false, null);
        List<TaskResponseDto> tasks = List.of(responseDto);

        Mockito.when(taskCollectionService.findAll(userPrincipal.id()))
                .thenReturn(tasks);

        mockMvc.perform(get("/tasks")
                        .with(user(userPrincipal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("test"))
                .andExpect(jsonPath("$[0].text").value("test"))
                .andExpect(jsonPath("$[0].isReady").value(false));
    }

    @Test
    void shouldReturn401UnauthorizedWithoutAuthorization() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Для доступа к этому ресурсу требуется полная аутентификация"));
    }

    @Test
    void shouldReturn401UnauthorizedWithIncorrectToken() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        Mockito.when(taskCollectionService.findAll(userPrincipal.id()))
                .thenThrow(new CustomAuthenticationException("не удалось найти пользователя"));

        mockMvc.perform(get("/tasks")
                        .with(user(userPrincipal)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("не удалось найти пользователя"));
    }
}
