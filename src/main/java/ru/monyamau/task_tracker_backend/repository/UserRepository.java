package ru.monyamau.task_tracker_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.monyamau.task_tracker_backend.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Integer id);

    boolean existsUserByEmail(String email);
}