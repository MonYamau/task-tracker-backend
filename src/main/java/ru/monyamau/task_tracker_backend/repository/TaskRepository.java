package ru.monyamau.task_tracker_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.monyamau.task_tracker_backend.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
