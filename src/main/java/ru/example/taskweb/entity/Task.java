package ru.example.taskweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "priority")
    private String priority;
    @NotNull(message = "Поле даты создания обязательно для заполнения.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @NotNull(message = "Поле даты окончания обязательно для заполнения.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadLine;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Release> releases = new ArrayList<>();

}
