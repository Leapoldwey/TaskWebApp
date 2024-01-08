package ru.example.taskweb.service;

import org.springframework.stereotype.Service;
import ru.example.taskweb.entity.User;

import java.security.Principal;
import java.util.List;

public interface UserActionService {
    void writeLog(String action, Principal principal);
    void writeLog(String action);
}
