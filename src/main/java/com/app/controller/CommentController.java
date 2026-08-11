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
import java.util.Map;
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
        String username = null;
        if  (authentication != null) {
            username = authentication.getName();
        }
        return ResponseEntity.ok(ApiResponse.ok(commentMapper.toDto(commentService.findCommentById(id, username))));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> editComment(@PathVariable UUID id, @Valid @RequestBody CommentDto dto, Authentication authentication) {
        Comment updated = commentService.editComment(id, dto.getContent(),authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(commentMapper.toDto(updated)));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> removeComment(@PathVariable UUID id, Authentication authentication) {
        commentService.removeComment(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.message("Comment deleted successfully"));
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentDto>>> listCommentsByPost(@PathVariable UUID id, Authentication authentication) {
        String username = null;
        if  (authentication != null) {
            username = authentication.getName();
        }
        return ResponseEntity.ok(ApiResponse.ok(commentService.listCommentByPostId(id, username).stream().map(commentMapper::toDto).toList()));
    }

    @PutMapping("/comments/{id}/vote")
    public ResponseEntity<ApiResponse<CommentDto>> voteComment(@PathVariable UUID id, @RequestBody Map<String, String>requestBody, Authentication authentication) {
        String voteType = requestBody.get("voteType");
        Comment updated = commentService.voteComment(id, voteType, authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(commentMapper.toDto(updated)));
    }
}