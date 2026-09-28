package ru.monyamau.task_tracker_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.monyamau.task_tracker_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
