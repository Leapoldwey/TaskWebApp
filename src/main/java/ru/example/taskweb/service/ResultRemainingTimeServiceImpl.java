package ru.example.taskweb.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ru.example.taskweb.entity.RemainingTime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class ResultRemainingTimeServiceImpl implements RemainingTimeService {

     public void remainingTime(RemainingTime remainingTime, ModelAndView mav, BindingResult bindingResult) {
         try {
             LocalDate endDate = remainingTime.getEndDate();
             long daysRemaining = Duration.between(LocalDate.now().atStartOfDay(), endDate.atStartOfDay()).toDays();
             mav.addObject("daysRemaining", daysRemaining);
             mav.setViewName("ResultRemainingTime");
         } catch (DateTimeParseException e) {
             bindingResult.rejectValue("endDate", "error.endDate",
                     "Неправильный формат даты.");
             mav.setViewName("add-remainingTime-form");
         }
     }
}
