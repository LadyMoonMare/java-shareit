package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "app_users", schema = "public")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotNull
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "name",nullable = false)
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
