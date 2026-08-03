package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommunityDto {
    private UUID id;
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Community name must contain only letters, numbers, and '_'")
    @NotBlank(message = "Community name is required")
    @Size(min = 3, message = "Community name must have at least 3 characters")
    @Size(max = 50, message = "Community name is too long")
    private String name;
    @NotBlank(message = "Community display name is required")
    private String displayName;
    @NotBlank(message = "Description is required")
    private String description;
    private LocalDateTime createdAt;
}
