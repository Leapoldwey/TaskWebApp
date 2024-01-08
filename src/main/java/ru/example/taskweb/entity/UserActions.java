package ru.example.taskweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userActions")
public class UserActions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userEmail")
    private String userEmail;
    @Column(name = "date")
    private LocalDateTime dateActions;
    @Column(name = "Description")
    private String description;
}
