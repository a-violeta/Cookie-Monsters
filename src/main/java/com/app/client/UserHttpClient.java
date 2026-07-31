package com.app.client;

import com.app.dto.LoginRequest;
import com.app.dto.UserDto;
import com.app.model.User;
import com.app.service.UserUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

// the same way UserService has loggedInUser, so does this class
// but it s purely for cache, just so we don't cross the network just for this
// both are kept in sync by calling the server first then updating after the server confirms

// should delete method toDto implemented here and use the toDto from mapper instead

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class UserHttpClient implements UserUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    // client side cache
    private User loggedInUser = null;

    @Override
    public User createUser(String username, String email, String password, String description) {
        String url = clientConfig.getBaseUrl() + "/api/users";
        UserDto request = new UserDto();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setDescription(description);

        try {
            UserDto response = restTemplate.postForObject(url, request, UserDto.class);
            log.info("User created via HTTP: {}", username);
            return toUser(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to create user via HTTP: {}", username, e);
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public User login(String username, String password) {
        String url = clientConfig.getBaseUrl() + "/api/users/login";
        LoginRequest request = new LoginRequest();
        request.setIdentifier(username);
        request.setPassword(password);

        try {
            // set UserService's loggedInUser
            // that s the copy PostService/CommentService actually check to know who is logged in
            UserDto response = restTemplate.postForObject(url, request, UserDto.class);
            User user = toUser(response);
            this.loggedInUser = user; // keep our cache in sync
            log.info("Logged in via HTTP: {}", username);
            return user;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void logout() {
        String url = clientConfig.getBaseUrl() + "/api/users/logout";
        try {
            restTemplate.postForLocation(url, null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // even if the network call fails, clear local state anyway
            // the console shouldn't act "logged in" if it can't reach the server
            log.warn("Server logout call failed, clearing local state anyway", e);
        } finally {
            this.loggedInUser = null;
        }
    }

    @Override
    public User getLoggedInUser() {
        // no network call, answers from the cache
        // this can differ from the truth if the server restarts mid-session
        return this.loggedInUser;
    }

    private User toUser(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setDescription(dto.getDescription());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
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