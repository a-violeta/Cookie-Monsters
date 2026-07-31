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

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostUseCases postService;
    private final PostMapper postMapper;

    @PostMapping("/api/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto dto) {
        // dto.communityId/userId are plain ids
        // postService does the real lookup and membership check, never trusted directly from the client
        Post created = postService.addPost(dto.getCommunityId(), dto.getUserId(), dto.getTitle(), dto.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDto(created));
    }

    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable long postId) {
        return ResponseEntity.ok(postMapper.toDto(postService.findPostById(postId)));
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<Void> editPost(@PathVariable long postId, @RequestBody PostDto dto) {
        // authorship check is in PostUseCases
        postService.editPost(postId, dto.getText());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/communities/{communityId}/posts")
    public ResponseEntity<List<PostDto>> listPostsForCommunity(@PathVariable long communityId) {
        // the one nested route, listPosts(communityId), is scoped this way
        return ResponseEntity.ok(postService.listPosts(communityId).stream().map(postMapper::toDto).toList());
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<PostDto>> listAllPosts() {
        return ResponseEntity.ok(postService.listPosts().stream().map(postMapper::toDto).toList());
    }
}