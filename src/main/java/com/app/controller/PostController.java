package com.app.controller;

import com.app.dto.PostDto;
import com.app.mapper.PostMapper;
import com.app.model.Post;
import com.app.service.PostUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostUseCases postService;
    private final PostMapper postMapper;

    @PostMapping("/api/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto dto) {
        // Changed dto.getText() to dto.getContent()
        Post created = postService.addPost(dto.getCommunityId(), dto.getUserId(), dto.getTitle(), dto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDto(created));
    }

    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(postMapper.toDto(postService.findPostById(postId)));
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<Void> editPost(@PathVariable UUID postId, @RequestBody PostDto dto) {
        // Changed dto.getText() to dto.getContent()
        postService.editPost(postId, dto.getContent());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/communities/{communityId}/posts")
    public ResponseEntity<List<PostDto>> listPostsForCommunity(@PathVariable UUID communityId) {
        return ResponseEntity.ok(postService.listPosts(communityId).stream().map(postMapper::toDto).toList());
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<PostDto>> listAllPosts() {
        return ResponseEntity.ok(postService.listPosts().stream().map(postMapper::toDto).toList());
    }
}