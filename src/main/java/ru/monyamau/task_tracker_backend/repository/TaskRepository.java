package ru.monyamau.task_tracker_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.monyamau.task_tracker_backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
