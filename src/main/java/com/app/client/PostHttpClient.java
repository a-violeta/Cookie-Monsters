package com.app.client;

import com.app.dto.PostDto;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.response.ApiResponse;
import com.app.service.PostAbstract;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class PostHttpClient implements PostAbstract {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    @Override
    public void validatePostImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {

            // size validation (max 5 MB)
            long maxSizeBytes = 5 * 1024 * 1024;
            if (image.getSize() > maxSizeBytes) {
                throw new IllegalArgumentException("Image size must be less than 5 MB");
            }

            // format validation (only JPG and PNG)
            String contentType = image.getContentType();
            if (contentType == null || (!contentType.equals("image/jpg") && !contentType.equals("image/png"))) {
                throw new IllegalArgumentException("Only JPG and PNG formats are allowed");
            }
        }
    }

    public Post addPost(String title, String content, String subreddit, String requesterUsername,
                        MultipartFile image, Integer filter) {
        String url = clientConfig.getBaseUrl() + "/posts";

        PostDto request = new PostDto();
        request.setSubreddit(subreddit);
        request.setTitle(title);
        request.setContent(content);

        try {
            ResponseEntity<ApiResponse<PostDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<>() {
                    }
            );

            log.info("Post created via HTTP in community {}", subreddit);
            if (response.getBody() != null) {
                return toPost(response.getBody().getData());
            }
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to create post via HTTP", e);
            throw new RuntimeException("Failed to create post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Post findPostById(UUID postId, String requesterUsername) {
        String url = clientConfig.getBaseUrl() + "/posts/" + postId;

        try {
            ResponseEntity<ApiResponse<PostDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            if (response.getBody() != null) {
                return toPost(response.getBody().getData());
            }
            return null;
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Post with id " + postId + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public List<Post> listPosts(UUID communityId) {
        String url = clientConfig.getBaseUrl() + "/subreddits/" + communityId + "/posts";
        try {
            ResponseEntity<List<PostDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
            return response.getBody() != null ? response.getBody().stream().map(this::toPost).toList() : null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public List<Post> listPosts(String requesterUsername) {
        String url = clientConfig.getBaseUrl() + "/posts";
        try {
            ResponseEntity<ApiResponse<List<PostDto>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            if (response.getBody() != null) {
                return response.getBody().getData().stream().map(this::toPost).toList();
            }
            return new ArrayList<>();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Post editPost(UUID postId, String newTitle, String newContent, String requesterUsername) {
        String url = clientConfig.getBaseUrl() + "/posts/" + postId;
        PostDto request = new PostDto();
        request.setTitle(newTitle);
        request.setContent(newContent);
        try {
            ResponseEntity<ApiResponse<PostDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (response.getBody() != null) {
                return toPost(response.getBody().getData());
            }
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to edit post: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Post votePost(UUID postId, String voteType, String requesterUsername) {
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

    public List<Post> listPostsBySubreddit(String subreddit, String requesterUsername) {
        String url = clientConfig.getBaseUrl() + "/posts?subreddit=" + subreddit;
        try {
            ResponseEntity<ApiResponse<List<PostDto>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            if (response.getBody() != null) {
                return response.getBody().getData().stream().map(this::toPost).toList();
            }
            return new ArrayList<>();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to list posts: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public void deletePost(UUID postId, String requesterUsername) {
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