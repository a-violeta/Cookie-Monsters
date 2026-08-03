package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// why ids instead of objects:
// circular references would break JSON serialization
// Community has a list of Users, User has a list of Posts, Post has a Community ...
// it would leak data: a User has a password, why let that cross the wire?


@Data
public class PostDto {
    private UUID id;

    // ids reference to the parent Community/User
    // the server always re-derives the real relationships from communityId/userId
    // using the repositories, not trusting what the client sent
    // that's what keeps the Service classes' checks useful
    @NotNull(message = "Community id is required")
    private Long communityId;

    @NotNull(message = "User id is required")
    private Long userId;

    // fields for display, populated on responses only, ignored on requests if blank
    // so the console has something readable to print without a second lookup
    // if a client sends a create/update PostDto without these,
    // the server ignores them and derives the real values from communityId/userId
    private String subreddit;
    private String author;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private LocalDateTime createdAt;
}