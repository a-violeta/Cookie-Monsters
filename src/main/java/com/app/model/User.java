package com.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private static long idIncrementor = 0;
    // it s static so that all Users objects have the same idIncrementor
    // and the static method incrementId() is called in the constructors to give a 'bigger' id everytime
    // so ids are unique
    // so the ids returned will be 1 for the first User created, then 2, 3...

    private final long userId;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String description;
    @Setter
    private LocalDateTime createdAt;

    // the validations for username and password can be made in UserService class
    // it is business logic

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    public User(){
        this.userId = incrementId();
        this.username="";
        this.password="";
        this.description="";
        this.createdAt=LocalDateTime.now();
    }

    public User(String username, String password, String description){
        this.userId = incrementId();
        this.username=username;
        this.password=password;
        this.description=description;
        this.createdAt=LocalDateTime.now();
    }

    public User(String username, String password, String description, LocalDateTime createdAt){
        this.userId = incrementId();
        this.username=username;
        this.password=password;
        this.description=description;
        this.createdAt=createdAt;
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
