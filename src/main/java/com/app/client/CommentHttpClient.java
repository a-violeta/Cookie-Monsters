package com.app.client;

import com.app.dto.CommentDto;
import com.app.model.Comment;
import com.app.model.Post;
import com.app.model.User;
import com.app.service.CommentUseCases;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class CommentHttpClient implements CommentUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    @Override
    public void validateComment(String text) {
        // pure logic, no I/O, safe to duplicate rather than round-trip the network
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    @Override
    public Comment addComment(String text, long userId, UUID postId) {
        validateComment(text);
        String url = clientConfig.getBaseUrl() + "/api/comments";

        CommentDto request = new CommentDto();
        request.setContent(text);
        request.setUserId(userId);
        request.setPostId(postId);

        try {
            CommentDto response = restTemplate.postForObject(url, request, CommentDto.class);
            log.info("Comment added via HTTP on post {}", postId);
            return toComment(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public Comment findCommentById(UUID commentId) {
        String url = clientConfig.getBaseUrl() + "/api/comments/" + commentId;
        try {
            return toComment(restTemplate.getForObject(url, CommentDto.class));
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void editComment(UUID commentId, String newText) {
        String url = clientConfig.getBaseUrl() + "/api/comments/" + commentId;
        CommentDto request = new CommentDto();
        request.setText(newText);
        try {
            restTemplate.put(url, request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void removeComment(UUID commentId) {
        String url = clientConfig.getBaseUrl() + "/api/comments/" + commentId;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public List<Comment> listComments() {
        String url = clientConfig.getBaseUrl() + "/api/comments";
        try {
            ResponseEntity<List<CommentDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentDto>>() {});
            return response.getBody().stream().map(this::toComment).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public List<Comment> listCommentByPostId(UUID postId) {
        String url = clientConfig.getBaseUrl() + "/api/comments/post/" + postId;
        try {
            ResponseEntity<List<CommentDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentDto>>() {});
            return response.getBody().stream().map(this::toComment).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    // detached objects for console display
    // never persisted, never re-queried, not real managed entities
    private Comment toComment(CommentDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getUserId());
        user.setUsername(dto.getUsername());

        Post post = new Post();
        post.setId(dto.getPostId());

        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(dto.getCreatedAt());
        return comment;
    }

    private String extractMessage(HttpClientErrorException e) {
        return e.getResponseBodyAsString().isBlank()
                ? "Request failed (" + e.getStatusCode() + ")"
                : e.getResponseBodyAsString();
    }

    private String extractMessage(Exception e) {
        return "Request failed: " + e.getMessage();
    }
}