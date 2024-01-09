package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskweb.entity.Release;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
    List<Release> findByTask(Task task);
    List<Release> findByUser(User user);
}
