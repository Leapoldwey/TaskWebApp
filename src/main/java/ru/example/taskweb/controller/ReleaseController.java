package ru.example.taskweb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.example.taskweb.dto.ReleaseDto;
import ru.example.taskweb.entity.Release;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;
import ru.example.taskweb.repository.ReleaseRepository;
import ru.example.taskweb.repository.TaskRepository;
import ru.example.taskweb.service.ReleaseDtoService;
import ru.example.taskweb.service.UserActionService;
import ru.example.taskweb.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ReleaseController {
    private static final Logger log = LogManager.getLogger(ReleaseController.class);
    @Autowired
    private final ReleaseRepository releaseRepository;
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final ReleaseDtoService releaseDtoService;
    @Autowired
    private final UserActionService userActionService;
    @Autowired
    private final UserService userService;

    public ReleaseController(ReleaseRepository releaseRepository, TaskRepository taskRepository,
                             ReleaseDtoService releaseDtoService, UserActionService userActionService, UserService userService) {
        this.releaseRepository = releaseRepository;
        this.taskRepository = taskRepository;
        this.releaseDtoService = releaseDtoService;
        this.userActionService = userActionService;
        this.userService = userService;
    }

    @GetMapping("/list/releases")
    public ModelAndView getAllReleases(Principal principal) {
        log.info("/list/releases -> connection");

        ModelAndView mav = new ModelAndView("/list-releases");

        User currentUser = userService.findByEmail(principal.getName());

        List<Release> releases = releaseRepository.findByUser(currentUser);
        mav.addObject("releases", releases);

        return mav;
    }

    @GetMapping("/users/listReleasesForAdmin")
    public ModelAndView getReleasesForAdmin(@RequestParam Long taskId) {
        log.info("/list/releases -> connection");

        ModelAndView mav = new ModelAndView("/list-releases-for-admin");

        Optional<Task> task = taskRepository.findById(taskId);

        User userName = userService.findByTask(task.get());

        mav.addObject("releases", releaseRepository.findByTask(task.get()));
        mav.addObject("username", userName.getEmail());
        return mav;
    }

    @GetMapping("addReleaseForm")
    public ModelAndView addReleaseForm(@RequestParam Long taskId) {
        ModelAndView mav = new ModelAndView("add-release-form");

        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setTaskId(taskId);

        mav.addObject("releaseDto", releaseDto);
        return mav;
    }

    @PostMapping("/saveRelease")
    public String saveRelease(@Valid @ModelAttribute ReleaseDto releaseDto, BindingResult bindingResult,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "add-release-form";
        }

        log.info("Добавление релиза пользователем - {}", principal.getName());
        userActionService.writeLog("Добавление релиза пользователем", principal);

        Release release;
        if (releaseDto.getId() != null) {
            release = releaseRepository.findById(releaseDto.getId())
                    .orElseThrow(() -> new RuntimeException("Релиз не найден"));
        } else {
            release = new Release();
        }
        User currencyUser = userService.findByEmail(principal.getName());
        releaseDtoService.setRelease(releaseDto, release, currencyUser);

        Task task = taskRepository.findById(releaseDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        release.setTask(task);

        releaseRepository.save(release);

        log.info("Релиз добавлен пользователем - {}", principal.getName());
        userActionService.writeLog("Релиз добавлен пользователем", principal);
        return "redirect:/list";
    }



    @GetMapping("/showUpdateReleaseForm")
    public ModelAndView updateRelease(@RequestParam Long releaseId, Principal principal) {
        ModelAndView mav = new ModelAndView("add-release-form");

        Optional<Release> optionalRelease = releaseRepository.findById(releaseId);
        ReleaseDto releaseDto = new ReleaseDto();

        if (optionalRelease.isPresent()) {
            releaseDtoService.setReleaseDto(releaseDto, optionalRelease, releaseId);
        }

        mav.addObject("releaseDto", releaseDto);
        return mav;
    }

    @GetMapping("/searchReleaseByTask")
    public ModelAndView searchReleaseByTask(@RequestParam Long taskId) {
        log.info("/list/releases -> connection");

        ModelAndView mav = new ModelAndView("/list-releases");

        Optional<Task> task = taskRepository.findById(taskId);

        mav.addObject("releases", releaseRepository.findByTask(task.get()));
        return mav;
    }

    @GetMapping("/deleteRelease")
    public String deleteRelease(@RequestParam Long releaseId) {
        releaseRepository.deleteById(releaseId);
        return "redirect:/list/releases";
    }

    @GetMapping("/deleteReleaseByAdmin")
    public String deleteReleaseByAdmin(@RequestParam Long releaseId, Principal principal) {
        log.info("Удаление релиза администратором");
        userActionService.writeLog("Удаление релиза администратором", principal);

        releaseRepository.deleteById(releaseId);

        log.info("Релиз удален администратором");
        userActionService.writeLog("Релиз удален администратором", principal);
        return "redirect:/users";
    }
}
