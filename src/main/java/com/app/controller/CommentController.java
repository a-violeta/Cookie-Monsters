package com.app.controller;

import com.app.dto.CommentDto;
import com.app.mapper.CommentMapper;
import com.app.model.Comment;
import com.app.service.CommentUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentUseCases commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID postId, @Valid @RequestBody CommentDto dto) {
        // CommentUseCases re-derives Post/User from the ids
        // CommentUseCases checks the person is a member of the post's community
        Comment created = commentService.addComment(dto.getContent(), dto.getUserId(), postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(created));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable UUID id) {
        return ResponseEntity.ok(commentMapper.toDto(commentService.findCommentById(id)));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Void> editComment(@PathVariable UUID id, @RequestBody CommentDto dto) {
        commentService.editComment(id, dto.getContent());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> removeComment(@PathVariable UUID id) {
        commentService.removeComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> listComments() {
        return ResponseEntity.ok(commentService.listComments().stream().map(commentMapper::toDto).toList());
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<CommentDto>> listCommentsByPost(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.listCommentByPostId(id).stream().map(commentMapper::toDto).toList());
    }
}