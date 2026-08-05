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

import java.util.Map;

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
    public User createUser(String username, String email, String password) {
        return null;
    }

    @Override
    public User login(String username, String password) {
        String url = clientConfig.getBaseUrl() + "/api/users/login";
        LoginRequest request = new LoginRequest();
        request.setIdentifier(username);
        request.setPassword(password);

        try {
            UserDto response = restTemplate.postForObject(url, request, UserDto.class);
            User user = toUser(response);
            this.loggedInUser = user;
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
            log.warn("Server logout call failed, clearing local state anyway", e);
        } finally {
            this.loggedInUser = null;
        }
    }

    @Override
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    // FIX: Added findByUsername (Required by AuthController)
    @Override
    public User findByUsername(String username) {
        String url = clientConfig.getBaseUrl() + "/api/users/" + username;
        try {
            UserDto response = restTemplate.getForObject(url, UserDto.class);
            return toUser(response);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("User with username " + username + " not found");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    // FIX: Added updateProfile (Required by AuthController)
    @Override
    public User updateProfile(String username, String displayName, String avatarUrl) {
        String url = clientConfig.getBaseUrl() + "/api/users/" + username + "/profile";
        Map<String, String> request = Map.of(
                "displayName", displayName != null ? displayName : "",
                "avatarUrl", avatarUrl != null ? avatarUrl : ""
        );

        try {
            restTemplate.put(url, request);
            return findByUsername(username);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    // FIX: Added changePassword (Required by AuthController)
    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        String url = clientConfig.getBaseUrl() + "/api/users/" + username + "/password";
        Map<String, String> request = Map.of(
                "currentPassword", currentPassword,
                "newPassword", newPassword
        );

        try {
            restTemplate.put(url, request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
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