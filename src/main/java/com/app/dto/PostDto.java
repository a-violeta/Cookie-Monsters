package com.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

// why ids instead of objects:
// circular references would break JSON serialization
// Community has a list of Users, User has a list of Posts, Post has a Community ...
// it would leak data: a User has a password, why let that cross the wire?


@Data
public class PostDto {
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title must have at least 3 characters")
    @Size(max = 300, message = "Title is too long")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 10000, message = "Content is too long")
    private String content;

    @JsonIgnore
    private MultipartFile image;

    private String imageUrl;

    private Integer filter;

    // fields for display, populated on responses only, ignored on requests if blank
    // so the console has something readable to print without a second lookup
    private String author;

    @NotNull(message = "Subreddit name is required")
    private String subreddit;

    private long upvotes;
    private long downvotes;
    private long score;

    private long commentCount;

    private String userVote;

    private Instant createdAt;
    private Instant updatedAt;
}