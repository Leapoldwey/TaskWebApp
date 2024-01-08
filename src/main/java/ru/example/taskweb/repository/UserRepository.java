package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.taskweb.entity.Role;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByTasks(Task task);
}
