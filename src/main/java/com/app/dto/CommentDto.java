package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

// why id instead of the whole object? check out explanation in PostDto

@Data
public class CommentDto {
    private Long id;

    @NotBlank(message = "Text is required")
    private String text;

    // these are not full objects anymore because they carry back references to each other
    // and because they are flat, we need a mapper to create the full object
    @NotNull(message = "User id is required")
    private Long userId;

    // the server always re-derives the real relationships from communityId/userId
    // using the repositories, not trusting what the client sent
    // that's what keeps the Service classes checks useful instead of bypassable
    @NotNull(message = "Post id is required")
    private Long postId;

    private String username; // convenience display field, response-only

    private LocalDateTime createdAt;
}