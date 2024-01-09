package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByTasks(Task task);
}
