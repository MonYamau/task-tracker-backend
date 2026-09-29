package ru.monyamau.task_tracker_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

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

    @Column(name = "TIME")
    private OffsetDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER", referencedColumnName = "ID", nullable = false)
    private User owner;

    public Task(String title, String text, boolean isReady, User owner, OffsetDateTime completedAt) {
        this.title = title;
        this.text = text;
        this.isReady = isReady;
        this.completedAt = completedAt;
        this.owner = owner;
    }
}
