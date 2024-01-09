package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskweb.entity.UserActions;

public interface UserActionRepository extends JpaRepository<UserActions, Long> {
}
