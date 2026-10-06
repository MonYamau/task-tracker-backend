package ru.monyamau.task_tracker_backend.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.monyamau.task_tracker_backend.exception.AuthenticationException;
import ru.monyamau.task_tracker_backend.exception.InvalidInputException;

public class JwtTokenProviderTest {
    @Test
    @DisplayName("Успешное создание и валидация токена")
    public void shouldCreateJwtTokenSuccessfully() {
        String email = "test@gmail.com";
        JwtTokenProvider tokenProvider = new JwtTokenProvider("secret", 10);
        String token = tokenProvider.createFormattedToken(1, email);
        Assertions.assertTrue(token != null && !token.isBlank());
        UserPrincipal principal = tokenProvider.authenticateWithToken(token.substring(7)).get();
        Assertions.assertEquals(1, (int) principal.id());
        Assertions.assertEquals(email, principal.getUsername());
    }

    @Test
    @DisplayName("Исключение AuthenticationException в случае истекшего токена")
    public void shouldNotAuthenticateExpiredToken() {
        String email = "test@gmail.com";
        JwtTokenProvider tokenProvider = new JwtTokenProvider("secret", 0);
        String token = tokenProvider.createFormattedToken(1, email);
        Assertions.assertTrue(token != null && !token.isBlank());
        Assertions.assertThrows(AuthenticationException.class, () -> tokenProvider.authenticateWithToken(token.substring(7)));
    }

    @Test
    @DisplayName("Исключение InvalidInputException в случае невалидного токена")
    public void shouldNotAuthenticateInvalidToken() {
        String email = "test@gmail.com";
        JwtTokenProvider tokenProvider = new JwtTokenProvider("secret", 10);
        String token = tokenProvider.createFormattedToken(1, email);
        Assertions.assertTrue(token != null && !token.isBlank());
        Assertions.assertThrows(InvalidInputException.class, () -> tokenProvider.authenticateWithToken(token));
    }
}