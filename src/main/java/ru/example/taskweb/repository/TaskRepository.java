package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;

import java.util.List;
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserId(Long userId);

}
