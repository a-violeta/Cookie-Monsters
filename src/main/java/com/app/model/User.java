package com.app.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // create table "users" for this class
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    //no id generator
    //let database make it for no errors in future
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long userId; // JPA needs to be null before using

    private String username;
    private String password;
    private String description;
    private LocalDateTime createdAt;

    //constructors
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String password, String description) {
        this.username = username;
        this.password = password;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String password, String description, LocalDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.description = description;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}