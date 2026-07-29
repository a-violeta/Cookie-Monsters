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

/**
 * UserService keeps a loggedInUser field server-side
 * that's the actual source of truth for "who is logged in," and it's what
 * PostService/CommentService check against for authorship. This class ALSO
 * keeps a loggedInUser field, but that's purely a client-side cache so
 * getLoggedInUser() can answer instantly without a network round trip on
 * every check. login()/logout() keep both copies in sync by calling the
 * server first, then updating the local field only after the server confirms.
 */

// should delete method toDto implemented here and use the toDto from mapper instead

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class UserHttpClient implements UserUseCases {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;

    // client-side cache, mirrors what the server's login call just set on its own singleton
    private User loggedInUser = null;

    @Override
    public User createUser(String username, String email, String password, String description) {
        String url = clientConfig.getBaseUrl() + "/api/users";
        UserDto request = new UserDto();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setDescription(description);

        // Step b: call the server; UserController.createUser -> UserService.createUser
        // runs the real validation (blank checks, email format, uniqueness) we don't
        // duplicate here -- unlike CommunityHttpClient.validateCommunity, this
        // validation genuinely needs the database (existsByUsername/existsByEmail),
        // so it can't be safely duplicated client-side.
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
            // Server-side, this sets UserService's loggedInUser field --
            // that's the copy PostService/CommentService actually check.
            UserDto response = restTemplate.postForObject(url, request, UserDto.class);
            User user = toUser(response);
            this.loggedInUser = user; // keep our client-side cache in sync
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
            // Even if the network call fails, clear local state anyway --
            // the console shouldn't act "logged in" if it can't reach the server.
            log.warn("Server logout call failed, clearing local state anyway", e);
        } finally {
            this.loggedInUser = null;
        }
    }

    @Override
    public User getLoggedInUser() {
        // No network call -- answers from the client-side cache set by login().
        // This can drift from the server's real state if the server restarts
        // mid-session (its loggedInUser resets to null, ours doesn't know that).
        return this.loggedInUser;
    }

    /**
     * Detached User for display — not a managed entity, console has no datasource.
     */
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