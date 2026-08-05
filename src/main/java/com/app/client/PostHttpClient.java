package com.app.client;

import com.app.dto.PostDto;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.service.PostUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class PostHttpClient implements PostUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    @Override
    public void validatePost(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content is required");
        }
    }

    public Post addPost(String title, String content, String subreddit, String username) {
        validatePost(title, content);
        String url = clientConfig.getBaseUrl() + "/posts";

        PostDto request = new PostDto();
        request.setSubreddit(subreddit);
        request.setAuthor(username);
        request.setTitle(title);
        request.setContent(content);

        try {
            PostDto response = restTemplate.postForObject(url, request, PostDto.class);
            log.info("Post created via HTTP in community {}", subreddit);
            return toPost(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to create post via HTTP", e);
            throw new RuntimeException("Failed to create post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Post findPostById(UUID postId) {
        String url = clientConfig.getBaseUrl() + "/posts/" + postId;

        try {
            PostDto response = restTemplate.getForObject(url, PostDto.class);
            return toPost(response);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Post with id " + postId + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public List<Post> listPosts() {
        String url = clientConfig.getBaseUrl() + "/posts";
        try {
            ResponseEntity<List<PostDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
            });
            return response.getBody() != null ? response.getBody().stream().map(this::toPost).toList() : null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Post editPost(UUID postId, String newContent) {
        String url = clientConfig.getBaseUrl() + "/posts/" + postId;
        PostDto request = new PostDto();
        request.setContent(newContent);

        try {
            restTemplate.put(url, request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to edit post: " + e.getResponseBodyAsString(), e);
        }

        return toPost(request);
    }

    @Override
    public Post votePost(UUID postId, String voteType) {
        String url = clientConfig.getBaseUrl() + "/posts/" + postId + "/vote";

        Map<String, String> requestBody = Map.of("voteType", voteType);

        try {
            HttpEntity<Map<String, String>> requestEntity =
                    new HttpEntity<>(requestBody);

            ResponseEntity<PostDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    PostDto.class
            );

            return toPost(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to vote post via HTTP", e);
            throw new RuntimeException("Failed to vote on post: " + e.getResponseBodyAsString(), e);
        }
    }

    public List<Post> listPostsBySubreddit(String subreddit) {
        String url = clientConfig.getBaseUrl() + "/posts?subreddit=" + subreddit;
        try {
            ResponseEntity<List<PostDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            return response.getBody() != null ? response.getBody().stream().map(this::toPost).toList() : null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public void deletePost(UUID postId) {
        String url = clientConfig.getBaseUrl() + "/posts/" + postId;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to delete post: " + e.getResponseBodyAsString(), e);
        }
    }

    // builds a Post for displaying, along with a Community and a User
    private Post toPost(PostDto dto) {
        if (dto == null) return null;

        Community subreddit = new Community();
        subreddit.setName(dto.getSubreddit());

        User author = new User();
        author.setUsername(dto.getAuthor());

        Post post = new Post();
        post.setId(dto.getId());
        post.setSubreddit(subreddit);
        post.setAuthor(author);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUpvotes(dto.getUpvotes());
        post.setDownvotes(dto.getDownvotes());
        post.setScore(dto.getScore());

        post.setCommentCount(dto.getCommentCount());

        post.setUserVote(dto.getUserVote());
        post.setCreatedAt(dto.getCreatedAt());
        post.setUpdatedAt(dto.getUpdatedAt());

        return post;
    }
}