package com.app.dto;

import com.app.model.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// why id instead of the whole object? check out explanation in PostDto

@Data
public class CommentDto {

    @NotNull
    private UUID id;

    @NotNull
    private UUID postId;

    private UUID parentId;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content is too long")
    private String content;

    // these are not full objects anymore because they carry back references to each other
    // and because they are flat, we need a mapper to create the full object

    @NotBlank(message =  "Author is required")
    private String author;

    private long upvotes;
    private long downvotes;
    private long score;
    private String userVote;

    // the server always re-derives the real relationships from communityId/userId
    // using the repositories, not trusting what the client sent
    // that's what keeps the Service classes checks useful instead of bypassable

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentDto> replies;
}