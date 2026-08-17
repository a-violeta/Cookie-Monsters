package com.app.client;

import com.app.dto.AuthRequests.LoginRequest; // Make sure this points to the updated AuthRequests nested class
import com.app.dto.AuthResponseDto;
import com.app.dto.UserDto;
import com.app.model.User;
import com.app.response.ApiResponse;
import com.app.service.UserAbstract;
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

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "true")
public class UserHttpClient implements UserAbstract {

    private final RestTemplate restTemplate;
    private final HttpClientConfig clientConfig;
    private final AuthTokenHolder authTokenHolder;

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
            // updated to unwrap ApiResponse<UserDto>
            ResponseEntity<ApiResponse<UserDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<UserDto>>() {}
            );

            log.info("User created via HTTP: {}", username);
            return toUser(response.getBody().getData());
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
    public User createUser(String username, String email, String password, java.time.LocalDate dateOfBirth) {
        // console seeding doesn't need age validation - unused here, same as the 3-arg overload above
        return null;
    }

    @Override
    public User login(String username, String password) {
        String url = clientConfig.getBaseUrl() + "/auth/login";
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        try {
            ResponseEntity<ApiResponse<AuthResponseDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<AuthResponseDto>>() {}
            );

            AuthResponseDto authData = response.getBody().getData();

            User user = new User();
            user.setUsername(authData.getUser().getUsername());
            user.setEmail(authData.getUser().getEmail());
            user.setDisplayName(authData.getUser().getDisplayName());

            this.loggedInUser = user;
            this.authTokenHolder.setToken(authData.getAccessToken());
            log.info("Logged in via HTTP: {}", username);
            return user;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void logout() {
        this.loggedInUser = null;
        this.authTokenHolder.clear();
        log.info("Logged out locally from HTTP client.");
    }

    @Override
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

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

    @Override
    public User updateProfile(String username, String displayName, String avatarUrl) {
        // matches AuthController: PUT /auth/me (derives user from the JWT, not a path variable)
        String url = clientConfig.getBaseUrl() + "/auth/me";
        com.app.dto.AuthRequests.UpdateProfileRequest request = new com.app.dto.AuthRequests.UpdateProfileRequest();
        request.setDisplayName(displayName);
        request.setAvatarUrl(avatarUrl);

        try {
            ResponseEntity<ApiResponse<AuthResponseDto.UserDetails>> response = restTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<AuthResponseDto.UserDetails>>() {});
            AuthResponseDto.UserDetails details = response.getBody().getData();

            User user = new User();
            user.setUsername(details.getUsername());
            user.setEmail(details.getEmail());
            user.setDisplayName(details.getDisplayName());
            this.loggedInUser = user;
            return user;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        // matches AuthController: PUT /auth/me/password
        String url = clientConfig.getBaseUrl() + "/auth/me/password";
        com.app.dto.AuthRequests.ChangePasswordRequest request = new com.app.dto.AuthRequests.ChangePasswordRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException(extractMessage(e));
        }
    }

    @Override
    public void deleteAccount(String username, String password) {
        // matches AuthController: DELETE /auth/me, now requires password confirmation
        String url = clientConfig.getBaseUrl() + "/auth/me";
        com.app.dto.AuthRequests.DeleteAccountRequest request = new com.app.dto.AuthRequests.DeleteAccountRequest();
        request.setPassword(password);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(request), Void.class);
            this.loggedInUser = null;
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