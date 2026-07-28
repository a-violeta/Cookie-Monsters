package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    @NotBlank(message = "Text is required")
    private String text;

    // these are not full objects anymore because they carry back references to each other
    // and because they are flat, we need a mapper to create the full object
    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Post id is required")
    private Long postId;

    private String username; // convenience display field, response-only

    private LocalDateTime createdAt;
}