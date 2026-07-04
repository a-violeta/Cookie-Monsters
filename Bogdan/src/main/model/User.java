package main.model;

import java.time.LocalDate;

public class User {
    // used for auto-incrementing the user id for each new user
    private static int nextId = 1;

    private int userId;
    private String username;
    private String password;
    private String description;
    private String creationDate;

    public User(String username, String password, String description) {
        this.userId = nextId++;

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must have at least 3 characters");
        }

        if (!username.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("Username must contain only letters, numbers, '-' and '_'");
        }

        this.username = username;

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must have at least 8 characters");
        }

        this.password = password;

        this.description = description;

        this.creationDate = LocalDate.now().toString();
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return
                "\nUser id: " + userId + '\n' +
                "Username: " + username + '\n' +
                "Password: " + password + '\n' +
                "Description: " + description + '\n' +
                "Creation date: " + creationDate ;
    }
}
