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
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostUseCases postService;
    private final PostMapper postMapper;

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto dto) {
        // dto.communityId/userId are plain ids
        // postService does the real lookup and membership check, never trusted directly from the client
        Post created = postService.addPost(dto.getCommunityId(), dto.getUserId(), dto.getTitle(), dto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDto(created));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(postMapper.toDto(postService.findPostById(postId)));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> editPost(@PathVariable UUID postId, @RequestBody PostDto dto) {
        // authorship check is in PostUseCases
        postService.editPost(postId, dto.getContent());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subreddits/{communityId}/posts")
    public ResponseEntity<List<PostDto>> listPostsForCommunity(@PathVariable long communityId) {
        // the one nested route, listPosts(communityId), is scoped this way
        return ResponseEntity.ok(postService.listPosts(communityId).stream().map(postMapper::toDto).toList());
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> listAllPosts(@RequestParam(required = false) String subreddit) {
        return ResponseEntity.ok(postService.listPosts().stream().map(postMapper::toDto).toList());
    }

    @PutMapping("/posts/{id}/vote")
    public ResponseEntity<PostDto> votePost(@PathVariable UUID id, @RequestBody Map<String, String> requestBody) {
        String voteType = requestBody.get("voteType");
        Post updated = postService.votePost(id, voteType);
        return ResponseEntity.ok(postMapper.toDto(updated));
    }
}