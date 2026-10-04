package ru.monyamau.task_tracker_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.monyamau.task_tracker_backend.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findTaskByIdAndOwnerId(Integer id, Integer ownerId);

    List<Task> findAllByOwnerId(Integer ownerId);
}