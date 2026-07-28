package com.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "User name must contain only letters, numbers, and '_'")
    @NotBlank(message = "User name is required")
    @Size(min = 3, message = "User name must have at least 3 characters")
    @Size(max = 21, message = "User name is too long")
    // just copied requirements from CommunityName for now
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // accepted on requests, never serialized in responses
    private String password;

    @NotBlank(message = "Description is required")
    private String description;

    private LocalDateTime createdAt;
}