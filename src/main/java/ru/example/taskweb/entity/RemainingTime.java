package ru.example.taskweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class RemainingTime {
    @NotNull(message = "Поле даты окончания обязательно для заполнения.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
