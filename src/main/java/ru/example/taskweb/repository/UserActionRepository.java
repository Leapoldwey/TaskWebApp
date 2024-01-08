package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.taskweb.entity.UserActions;

import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserActions, Long> {
}
