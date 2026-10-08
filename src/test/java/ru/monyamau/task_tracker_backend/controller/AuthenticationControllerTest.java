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
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;
import ru.monyamau.task_tracker_backend.filter.JwtFilter;
import ru.monyamau.task_tracker_backend.security.JwtTokenProvider;
import ru.monyamau.task_tracker_backend.service.AuthenticationService;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class AuthenticationControllerTest {
    private static final String EXAMPLE_EMAIL = "example@gmail.com";
    private static final String EXAMPLE_PASSWORD = "Password123";
    @MockitoBean
    JwtTokenProvider tokenProvider;
    @MockitoBean
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        String token = "exampleJwtToken";

        Mockito.when(authenticationService.authenticateUser(requestDto))
                .thenReturn(new TokenResponseDto(token));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", token));
    }

    @Test
    void shouldReturn400BadRequestForEmailInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("test_example", EXAMPLE_PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Невалидный формат электронной почты"));

        Mockito.verifyNoInteractions(authenticationService);
    }

    @Test
    void shouldReturn400BadRequestForBlankEmailInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("", EXAMPLE_PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Электронная почта не может отсутствовать"));

        Mockito.verifyNoInteractions(authenticationService);
    }

    @Test
    void shouldReturn400BadRequestForPasswordInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, "русскийПароль123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Пароль может содержать только латинские буквы, цифры и некоторые спецсимволы"));

        Mockito.verifyNoInteractions(authenticationService);
    }

    @Test
    void shouldReturn400BadRequestForBlankPasswordInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists());

        Mockito.verifyNoInteractions(authenticationService);
    }

    @Test
    void shouldReturn401Unauthorized() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        Mockito.when(authenticationService.authenticateUser(requestDto))
                .thenThrow(new CustomAuthenticationException("неверное имя или пароль"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("неверное имя или пароль"));
    }

    @Test
    void shouldReturn500InternalError() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        Mockito.when(authenticationService.authenticateUser(requestDto))
                .thenThrow(new IllegalStateException("непредвиденная ошибка сервера"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Возникла ошибка на стороне сервера"));
    }
}