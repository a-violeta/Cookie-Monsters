package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {
    private Long id;

    @Pattern(regexp = "^\\S+$", message = "Spaces are not allowed")
    @NotBlank(message = "User name is required")
    @Size(min = 3, message = "User name must have at least 3 characters")
    @Size(max = 20, message = "User name is too long")
    // just copied requirements from CommunityName for now
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotBlank(message = "Description is required")
    private String description;

    private Instant createdAt;
}