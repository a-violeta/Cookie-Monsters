package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;

    // these are not full objects anymore because they carry back references to each other
    // and because they are flat, we need a mapper to create the full object
    @NotNull(message = "Community id is required")
    private Long communityId;

    @NotNull(message = "User id is required")
    private Long userId;

    // convenience fields for display, populated on responses only, ignored on requests if blank
    private String communityName;
    private String username;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Text is required")
    private String text;

    private LocalDateTime createdAt;
}