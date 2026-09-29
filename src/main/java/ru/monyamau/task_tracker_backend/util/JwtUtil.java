package ru.monyamau.task_tracker_backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.monyamau.task_tracker_backend.exception.AuthenticationException;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@UtilityClass
public final class JwtUtil {
    private static final String SUBJECT_NAME = "User details";
    private static final String ISSUER_NAME = "task-tracker";
    private static final String USERNAME_CLAIM = "username";

    @Value("${jwt.secret}")
    private static String secret;

    public static String createToken(String username) {
        ZonedDateTime time = ZonedDateTime.now();
        try {
            return JWT.create()
                    .withSubject(SUBJECT_NAME)
                    .withClaim(USERNAME_CLAIM, username)
                    .withIssuer(ISSUER_NAME)
                    .withIssuedAt(Date.from(time.toInstant()))
                    .withExpiresAt(Date.from(time.plusMinutes(60).toInstant()))
                    .sign(Algorithm.HMAC256(secret));
        } catch (Exception e) {
            log.error("Не удалось создать JWT токен для пользователя {}", username);
            throw new IllegalStateException("Failed to create a JWT token for user", e);
        }
    }

    public static Optional<String> authenticateWithToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(ISSUER_NAME)
                .withSubject(SUBJECT_NAME)
                .withClaimPresence(USERNAME_CLAIM)
                .build();
        try {
            return Optional.of(verifier
                    .verify(token)
                    .getClaim(USERNAME_CLAIM)
                    .asString());
        } catch (JWTVerificationException e) {
            throw new AuthenticationException("Failed to authenticate user: invalid token");
        }

    }
}
