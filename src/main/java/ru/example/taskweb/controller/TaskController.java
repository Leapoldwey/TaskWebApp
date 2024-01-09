package ru.example.taskweb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;
import ru.example.taskweb.repository.TaskRepository;
import ru.example.taskweb.service.UserActionService;
import ru.example.taskweb.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class TaskController {
    Logger log = LogManager.getLogger(TaskController.class);
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserActionService userActionService;

    public TaskController(TaskRepository taskRepository, UserService userService,
                          UserActionService userActionService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.userActionService = userActionService;
    }


    @GetMapping("/list")
    public ModelAndView getAllTasks(Principal principal) {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("/list-tasks");

        User currentUser = userService.findByEmail(principal.getName());

        List<Task> tasks = taskRepository.findByUser(currentUser);

        mav.addObject("tasks", tasks);
        return mav;
    }

    @GetMapping("/users/listTasksForAdmin")
    public ModelAndView getTasksForAdmin(@RequestParam String userEmail) {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("/list-tasks-for-admin");

        User findByEmail = userService.findByEmail(userEmail);

        List<Task> tasks = taskRepository.findByUserId(findByEmail.getId());

        mav.addObject("tasks", tasks);
        mav.addObject("username", userEmail);
        return mav;
    }

    @GetMapping("addTaskForm")
    public ModelAndView addTaskForm() {
        ModelAndView mav = new ModelAndView("add-task-form");

        Task task = new Task();

        mav.addObject("task", task);
        return mav;
    }
    @PostMapping("/saveTask")
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "add-task-form";
        }

        log.info("Добавление задачи пользователем - {}", principal.getName());
        userActionService.writeLog("Добавление задачи пользователем", principal);

        User currentUser = userService.findByEmail(principal.getName());

        task.setUser(currentUser);
        taskRepository.save(task);

        log.info("Задача добалена пользователем - {}", principal.getName());
        userActionService.writeLog("Задача добавлена пользователем", principal);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateTaskForm")
    public ModelAndView showUpdateTaskForm(@RequestParam Long taskId, Principal principal) {
        log.info("Редактирование задачи");
        userActionService.writeLog("Редактирование задачи", principal);

        ModelAndView mav = new ModelAndView("add-task-form");

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Task task = new Task();

        if (optionalTask.isPresent()) {
            task = optionalTask.get();
        }

        mav.addObject("task", task);
        return mav;
    }

    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam Long taskId, Principal principal) {
        taskRepository.deleteById(taskId);
        return "redirect:/list";
    }

    @GetMapping("/deleteTaskByAdmin")
    public String deleteTaskByAdmin(@RequestParam Long taskId, Principal principal) {
        log.info("Удаление задачи администратором");
        userActionService.writeLog("Удаление задачи администратором", principal);

        taskRepository.deleteById(taskId);

        log.info("Задача удалена администратором");
        userActionService.writeLog("Задача удалена администратором", principal);
        return "redirect:/users";
    }
}
