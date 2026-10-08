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
import ru.monyamau.task_tracker_backend.dto.request.UserRequestDto;
import ru.monyamau.task_tracker_backend.dto.response.TokenResponseDto;
import ru.monyamau.task_tracker_backend.exception.UserAlreadyExistsException;
import ru.monyamau.task_tracker_backend.filter.JwtFilter;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;
import ru.monyamau.task_tracker_backend.service.UserService;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class UserControllerTest {
    private static final String EXAMPLE_EMAIL = "example@gmail.com";
    private static final String EXAMPLE_PASSWORD = "Password123";
    @MockitoBean
    JwtTokenProvider tokenProvider;
    @MockitoBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        String token = "exampleJwtToken";

        Mockito.when(userService.registerUser(requestDto))
                .thenReturn(new TokenResponseDto(token));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Authorization", token));
    }

    @Test
    void shouldReturn400BadRequestForEmailInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("test_example", EXAMPLE_PASSWORD);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Невалидный формат электронной почты"));

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void shouldReturn400BadRequestForBlankEmailInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("", EXAMPLE_PASSWORD);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Электронная почта не может отсутствовать"));

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void shouldReturn400BadRequestForPasswordInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, "русскийПароль123");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Пароль может содержать только латинские буквы, цифры и некоторые спецсимволы"));

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void shouldReturn400BadRequestForBlankPasswordInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, "");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists());

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void shouldReturn409ConflictForNonUniqueEmail() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        Mockito.when(userService.registerUser(requestDto))
                .thenThrow(new UserAlreadyExistsException("пользователь уже существует"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("пользователь уже существует"));
    }

    @Test
    void shouldReturn500InternalServerError() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        Mockito.when(userService.registerUser(requestDto))
                .thenThrow(new IllegalStateException("неизвестная ошибка сервера"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Возникла ошибка на стороне сервера"));
    }

    @Test
    void shouldShowUserSuccessfully() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        mockMvc.perform(get("/user")
                        .with(user(userPrincipal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("email").value(EXAMPLE_EMAIL));
    }

    @Test
    void shouldReturn401UnauthorizedWithoutAuthorization() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Для доступа к этому ресурсу требуется полная аутентификация"));
    }
}