package ru.monyamau.task_tracker_backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import ru.monyamau.task_tracker_backend.infrastructure.LetterPublisher;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTestContext {
    @MockitoBean
    protected LetterPublisher letterPublisher;

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    static {
        POSTGRES_SQL_CONTAINER.start();
    }
}