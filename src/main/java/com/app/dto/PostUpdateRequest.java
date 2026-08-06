package com.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostUpdateRequest {
    @Size(min = 3, message = "Title must have at least 3 characters")
    @Size(max = 300, message = "Title is too long")
    private String title;

    @Size(max = 10000, message = "Content is too long")
    private String content;
}