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

    private UUID id;

    private UUID postId;

    private UUID parentId;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content is too long")
    private String content;
    private String author;
    private long upvotes;
    private long downvotes;
    private long score;
    private String userVote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentDto> replies;
}