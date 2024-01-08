package ru.example.taskweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.example.taskweb.entity.RemainingTime;
import ru.example.taskweb.service.RemainingTimeService;

import javax.validation.Valid;

@Controller
public class RemainingTimeController {
    @Autowired
    private final RemainingTimeService remainingTimeService;

    public RemainingTimeController(RemainingTimeService remainingTimeService) {
        this.remainingTimeService = remainingTimeService;
    }

    @GetMapping("/remainingTimeForm")
    public ModelAndView remainingTimeForm() {
        ModelAndView mav = new ModelAndView("add-remainingTime-form");
        RemainingTime remainingTime = new RemainingTime();
        mav.addObject("remainingTime", remainingTime);
        return mav;
    }

    @PostMapping("/resultRemainingTime")
    public ModelAndView resultRemainingTime(@Valid @ModelAttribute RemainingTime remainingTime,
                                            BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("add-remainingTime-form");
            return mav;
        }
        remainingTimeService.remainingTime(remainingTime, mav, bindingResult);
        return mav;
    }
}
