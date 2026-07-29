/**
 * HTTP client adapters for the four UseCases ports (User, Community, Post, Comment).
 *
 * THE FULL FLOW, END TO END:
 *
 * 1. A console Command holds a reference to a *UseCases interface (e.g. PostUseCases),
 *    never a concrete class. It doesn't know or care whether it's talking to a local
 *    service or a network client.
 *
 * 2. Spring decides WHICH implementation gets injected based on app.http.client.enabled:
 *      - false (server profile): the real *Service classes are active.
 *      - true  (console profile): these *HttpClient classes are active instead.
 *    Both sides carry a mirrored @ConditionalOnProperty so only one bean per
 *    interface ever exists in a given profile -- never both, never neither.
 *
 * 3. A *HttpClient method does three things, always in this order:
 *      a) Builds a request DTO from the plain arguments it received (the same
 *         arguments the interface method always took -- callers never notice
 *         a difference).
 *      b) Calls RestTemplate against clientConfig.getBaseUrl() + some path,
 *         wrapped in try/catch for HttpClientErrorException / HttpServerErrorException.
 *      c) Converts whatever DTO comes back into a domain object to return,
 *         since the interface's method signature promises a domain type
 *         (e.g. PostUseCases.addPost returns Post, not PostDto).
 *
 * 4. On the other end, the matching *Controller in com.app.controller receives
 *    the same DTO, unpacks its primitive fields, and calls the REAL local
 *    *Service -- the one with the actual repositories, validation, and
 *    membership/ownership checks. The controller never trusts a nested
 *    entity graph from the client; it always re-derives relationships
 *    (Community, User, etc.) via the service's own lookups.
 *
 * 5. The controller maps the resulting entity back to a DTO (via a *Mapper
 *    where the mapping is pure field-copying, or a small manual method where
 *    a field like `password` needs to be excluded) and returns it as JSON.
 *
 * 6. Back in the *HttpClient, step 3c happens: the DTO becomes a domain object.
 *    IMPORTANT: this object is a DETACHED, NON-PERSISTED holder, not a real
 *    JPA entity. The console process has no datasource in "console" profile --
 *    there is no repository to hydrate a real Community/User/Post/Comment from.
 *    These reconstructed objects exist only so console Commands can call
 *    getters (getUsername(), getCommunityName(), etc.) to print something --
 *    they should never be passed back into a *Service expecting a managed entity,
 *    and they're never saved anywhere.
 *
 * WHY EXCEPTIONS ARE TRANSLATED THE WAY THEY ARE:
 * A GlobalExceptionHandler (@RestControllerAdvice) on the server turns
 * IllegalArgumentException/IllegalStateException into 400/409 responses with
 * the original message as the body. Each *HttpClient catches
 * HttpClientErrorException/HttpServerErrorException and re-throws using
 * e.getResponseBodyAsString() as the message -- that string IS the original
 * service's error message, round-tripped through HTTP. Without the
 * exception handler, this would just be a generic Spring error blob instead
 * of something a console Command's consolePrinter can show meaningfully.
 */

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
 * User-specific note: UserService keeps a loggedInUser field server-side --
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