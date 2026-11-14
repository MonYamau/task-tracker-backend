package ru.monyamau.task_tracker_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.monyamau.task_tracker_backend.exception.InvalidInputException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "STATUS")
    private boolean isReady;

    @Column(name = "PLANNED_COMPLETION_AT", nullable = false)
    private OffsetDateTime plannedCompletionAt;

    @Column(name = "COMPLETED_AT")
    private OffsetDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER", referencedColumnName = "ID", nullable = false)
    private User owner;

    public Task(String title, String text, boolean isReady, OffsetDateTime plannedCompletionAt, User owner, OffsetDateTime completedAt) {
        this.title = title;
        this.text = text;
        this.isReady = isReady;
        this.plannedCompletionAt = plannedCompletionAt;
        this.completedAt = completedAt;
        this.owner = owner;
    }

    public void update(String title, String text, OffsetDateTime plannedCompletionAt, boolean isReady) {
        this.title = title;
        this.text = text;
        if (plannedCompletionAt != null && !plannedCompletionAt.equals(this.plannedCompletionAt)) {
            if (plannedCompletionAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS))) {
                throw new InvalidInputException("Нельзя запланировать выполнение задачи на прошлое");
            }
            this.plannedCompletionAt = plannedCompletionAt;
        }
        if (!this.isReady && isReady) {
            this.isReady = true;
            this.completedAt = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
        }
        if (this.isReady && !isReady) {
            this.isReady = false;
            this.completedAt = null;
        }
    }
}