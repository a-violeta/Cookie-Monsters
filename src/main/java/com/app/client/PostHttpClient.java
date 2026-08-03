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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class PostHttpClient implements PostUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    @Override
    public void validatePost(String title, String text) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text is required");
        }
    }

    @Override
    public Post addPost(long communityId, long userId, String title, String text) {
        validatePost(title, text);
        String url = clientConfig.getBaseUrl() + "/api/posts";

        PostDto request = new PostDto();
        request.setCommunityId(communityId);
        request.setUserId(userId);
        request.setTitle(title);
        request.setText(text);

        try {
            PostDto response = restTemplate.postForObject(url, request, PostDto.class);
            log.info("Post created via HTTP in community {}", communityId);
            return toPost(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to create post via HTTP", e);
            throw new RuntimeException("Failed to create post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Post findPostById(long postId) {
        String url = clientConfig.getBaseUrl() + "/api/posts/" + postId;
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
    public List<Post> listPosts(long communityId) {
        String url = clientConfig.getBaseUrl() + "/api/communities/" + communityId + "/posts";
        try {
            ResponseEntity<List<PostDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PostDto>>() {});
            return response.getBody().stream().map(this::toPost).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public List<Post> listPosts() {
        String url = clientConfig.getBaseUrl() + "/api/posts";
        try {
            ResponseEntity<List<PostDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PostDto>>() {});
            return response.getBody().stream().map(this::toPost).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public void editPost(long postId, String newText) {
        String url = clientConfig.getBaseUrl() + "/api/posts/" + postId;
        PostDto request = new PostDto();
        request.setText(newText);
        try {
            restTemplate.put(url, request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to edit post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public void deletePost(long postId) {
        String url = clientConfig.getBaseUrl() + "/api/posts/" + postId;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to delete post: " + e.getResponseBodyAsString(), e);
        }
    }

    // builds a Post for displaying, along with a Community and a User
    private Post toPost(PostDto dto) {
        if (dto == null) return null;

        Community community = new Community();
        community.setId(dto.getCommunityId());
        community.setName(dto.getCommunityName());

        User user = new User();
        user.setId(dto.getUserId());
        user.setUsername(dto.getUsername());

        Post post = new Post();
        post.setId(dto.getId());
        post.setCommunity(community);
        post.setUser(user);
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setCreatedAt(dto.getCreatedAt());
        return post;
    }
}