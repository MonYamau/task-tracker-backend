package ru.monyamau.task_tracker_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.monyamau.task_tracker_backend.entity.Task;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findTaskByIdAndOwnerId(Integer id, Integer ownerId);

    List<Task> findAllByOwnerId(Integer ownerId);

    @Query("""
            SELECT task
            FROM Task task
            JOIN FETCH task.owner
            WHERE (task.isReady = false AND task.plannedCompletionAt >= :from AND task.plannedCompletionAt < :to)
                        OR (task.isReady = true AND task.completedAt >= :from AND task.completedAt < :to)
            """)
    List<Task> findAllForReport(OffsetDateTime from, OffsetDateTime to);
}