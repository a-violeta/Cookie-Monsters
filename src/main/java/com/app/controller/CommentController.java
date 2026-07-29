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

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentUseCases commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto dto) {
        // commentUseCases re-derives Post/User from the ids
        // commentUseCases checks the commenter is a member of the post's community
        Comment created = commentService.addComment(dto.getText(), dto.getUserId(), dto.getPostId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(created));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentMapper.toDto(commentService.findCommentById(commentId)));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> editComment(@PathVariable long commentId, @RequestBody CommentDto dto) {
        commentService.editComment(commentId, dto.getText());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable long commentId) {
        commentService.removeComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> listComments() {
        return ResponseEntity.ok(commentService.listComments().stream().map(commentMapper::toDto).toList());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> listCommentsByPost(@PathVariable long postId) {
        return ResponseEntity.ok(commentService.listCommentByPostId(postId).stream().map(commentMapper::toDto).toList());
    }
}