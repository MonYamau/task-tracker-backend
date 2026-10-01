package ru.monyamau.task_tracker_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.monyamau.task_tracker_backend.exception.AuthenticationException;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public final class JwtTokenProvider {
    private static final String SUBJECT_NAME = "User details";
    private static final String ISSUER_NAME = "task-tracker";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER_TITLE = "Bearer ";

    private final String secret;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String createFormattedToken(Integer id, String email) {
        ZonedDateTime time = ZonedDateTime.now();
        try {
            String token = JWT.create()
                    .withSubject(SUBJECT_NAME)
                    .withClaim(ID_CLAIM, id)
                    .withClaim(EMAIL_CLAIM, email)
                    .withIssuer(ISSUER_NAME)
                    .withIssuedAt(Date.from(time.toInstant()))
                    .withExpiresAt(Date.from(time.plusMinutes(60).toInstant()))
                    .sign(Algorithm.HMAC256(secret));
            return BEARER_TITLE + token;
        } catch (Exception e) {
            log.error("Не удалось создать JWT токен для пользователя {}", email);
            throw new IllegalStateException("Не удалось создать JWT токен для пользователя", e);
        }
    }

    public Optional<String> authenticateWithToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(ISSUER_NAME)
                .withSubject(SUBJECT_NAME)
                .withClaimPresence(ID_CLAIM)
                .withClaimPresence(EMAIL_CLAIM)
                .build();
        try {
            return Optional.of(verifier
                    .verify(token)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (JWTVerificationException e) {
            throw new AuthenticationException("Не удалось аутентифицировать пользователя: токен не валиден");
        }
    }
}