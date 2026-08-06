package com.app.controller;

import com.app.dto.PostDto;
import com.app.dto.PostUpdateRequest;
import com.app.mapper.PostMapper;
import com.app.model.Post;
import com.app.response.ApiResponse;
import com.app.service.PostUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostUseCases postService;
    private final PostMapper postMapper;

    @GetMapping
    public ApiResponse<List<PostDto>> listAllPosts(@RequestParam(required = false) String subreddit) {
        if (subreddit == null) {
            return ApiResponse.ok(postService.listPosts().stream().map(postMapper::toDto).toList());
        }

        return ApiResponse.ok(postService.listPostsBySubreddit(subreddit).stream().map(postMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<PostDto> getPost(@PathVariable UUID id) {
        return ApiResponse.ok(postMapper.toDto(postService.findPostById(id)));
    }

    @PostMapping
    public ApiResponse<PostDto> createPost(@Valid @RequestBody PostDto dto) {
        Post created = postService.addPost(dto.getSubreddit(), dto.getAuthor(), dto.getSubreddit(), dto.getAuthor());
        return ApiResponse.ok(postMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ApiResponse<PostDto> editPost(@PathVariable UUID id, @RequestBody PostUpdateRequest dto) {
        // authorship check is in PostUseCases
        Post updated = postService.editPost(id, dto.getTitle(), dto.getContent());
        return ApiResponse.ok(postMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ApiResponse.message("The post was deleted successfully");
    }

    @PutMapping("/{id}/vote")
    public ApiResponse<PostDto> votePost(@PathVariable UUID id, @RequestBody Map<String, String> requestBody) {
        String voteType = requestBody.get("voteType");
        Post updated = postService.votePost(id, voteType);
        return ApiResponse.ok(postMapper.toDto(updated));
    }
}