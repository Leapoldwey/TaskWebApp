package ru.example.taskweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskweb.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
