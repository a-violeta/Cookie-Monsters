package com.app.controller;

import com.app.dto.CommentDto;
import com.app.mapper.CommentMapper;
import com.app.model.Comment;
import com.app.response.ApiResponse;
import com.app.service.CommentUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentUseCases commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentDto>> addComment(@PathVariable UUID postId, @Valid @RequestBody CommentDto dto,  Authentication authentication) {
        // CommentUseCases re-derives Post/User from the ids
        // CommentUseCases checks the person is a member of the post's community
        Comment created = commentService.addComment(dto.getContent(), postId, dto.getParentId(), authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(commentMapper.toDto(created)));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> getComment(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok(commentMapper.toDto(commentService.findCommentById(id, authentication.getName()))));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> editComment(@PathVariable UUID id, @Valid @RequestBody CommentDto dto, Authentication authentication) {
        Comment updated = commentService.editComment(id, dto.getContent(), authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(commentMapper.toDto(updated)));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> removeComment(@PathVariable UUID id, Authentication authentication) {
        commentService.removeComment(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.message("Comment deleted successfully"));
    }

    /*@GetMapping
    public ResponseEntity<ApiResponse<List<CommentDto>>> listComments() {
        return ResponseEntity.ok(ApiResponse.ok(commentService.listComments().stream().map(commentMapper::toDto).toList()));
    }*/

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentDto>>> listCommentsByPost(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.listCommentByPostId(id, authentication.getName()).stream().map(commentMapper::toDto).toList()));
    }
}