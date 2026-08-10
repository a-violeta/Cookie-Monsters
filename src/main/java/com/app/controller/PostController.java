package com.app.controller;

import com.app.dto.PostDto;
import com.app.dto.PostUpdateRequest;
import com.app.mapper.PostMapper;
import com.app.model.Post;
import com.app.response.ApiResponse;
import com.app.service.PostUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ApiResponse<List<PostDto>>> listAllPosts(@RequestParam(required = false) String subreddit,
                                                                   Authentication authentication) {
        if (subreddit == null) {
            return ResponseEntity
                    .ok(ApiResponse.ok(postService.listPosts(authentication.getName())
                            .stream().map(postMapper::toDto).toList()));
        }

        return ResponseEntity
                .ok(ApiResponse.ok(postService.listPostsBySubreddit(subreddit, authentication.getName())
                        .stream().map(postMapper::toDto).toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto>> getPost(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity
                .ok(ApiResponse.ok(postMapper.toDto(postService.findPostById(id, authentication.getName()))));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ApiResponse<PostDto> createPost(@Valid @ModelAttribute PostDto dto, Authentication authentication) {
        Post created = postService.addPost(
                dto.getTitle(),
                dto.getContent(),
                dto.getSubreddit(),
                authentication.getName(),
                dto.getImage(),
                dto.getFilter()
        );
        return ApiResponse.ok(postMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ApiResponse<PostDto> editPost(@PathVariable UUID id, @Valid @RequestBody PostUpdateRequest dto, Authentication authentication) {
        // authorship check is in PostUseCases
        Post updated = postService.editPost(id, dto.getTitle(), dto.getContent(), authentication.getName());
        return ApiResponse.ok(postMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable UUID id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return ApiResponse.message("The post was deleted successfully");
    }

    @PutMapping("/{id}/vote")
    public ApiResponse<PostDto> votePost(@PathVariable UUID id, @RequestBody Map<String, String> requestBody, Authentication authentication) {
        String voteType = requestBody.get("voteType");
        Post updated = postService.votePost(id, voteType, authentication.getName());
        return ApiResponse.ok(postMapper.toDto(updated));
    }
}