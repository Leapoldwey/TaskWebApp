package ru.example.taskweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.taskweb.entity.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseDto {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        @NotNull(message = "Поле даты создания обязательно для заполнения.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate releaseDate;
        private String description;
        private String version;

        private User user;
        private Long taskId;
}
