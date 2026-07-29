/**
 * REST controllers exposing the four UseCases ports over HTTP, for the console
 * (running as an HTTP client, see com.app.client) to call.
 *
 * THE RECEIVING HALF OF THE FLOW (see com.app.client's package-info.java for
 * the client side of this same story):
 *
 * 1. A request DTO arrives (UserDto, CommunityDto, PostDto, CommentDto, or
 *    LoginRequest), validated by @Valid against its @NotBlank/@NotNull/@Pattern
 *    annotations. Validation failures never reach a controller method body --
 *    they're caught by GlobalExceptionHandler.handleValidation() and turned
 *    into a 400 with the field error messages, before any service is called.
 *
 * 2. THE CONTROLLER NEVER TRUSTS A NESTED ENTITY GRAPH FROM THE CLIENT.
 *    PostDto/CommentDto carry communityId/userId/postId as plain ids, not
 *    embedded objects -- on purpose (see PostDto's own class comment). The
 *    controller unpacks those ids and passes them as primitive arguments
 *    straight to the real *Service (PostService, CommentService, etc.),
 *    which does its OWN repository lookups and its OWN validation
 *    (membership checks, authorship checks, uniqueness checks). This is
 *    why controllers call e.g. postService.addPost(dto.getCommunityId(),
 *    dto.getUserId(), dto.getTitle(), dto.getText()) instead of asking a
 *    mapper to build a Post directly from the DTO -- a mapper-built entity
 *    would silently bypass every one of those checks.
 *
 * 3. The *Service method runs exactly the same logic it always has, whether
 *    called from console-local mode or from a controller in server mode --
 *    the controller is a thin translation layer, not a second place where
 *    business rules live.
 *
 * 4. The resulting entity gets converted back to a response DTO -- either via
 *    a *Mapper (Community/Post/Comment: pure field-copying or association
 *    flattening, nothing to hide) or a small manual method (User: the one
 *    place a field -- password -- must never appear in the response, kept
 *    visible as a plain method rather than an annotation elsewhere).
 *
 * 5. If the service throws IllegalArgumentException or IllegalStateException
 *    (bad input, not found, not a member, not the author, etc.), the
 *    controller method itself does NOT catch it. GlobalExceptionHandler
 *    (@RestControllerAdvice) catches it centrally and turns it into a 400
 *    or 409 with the exception's message as the plain-text body. This is
 *    deliberate: without it, an uncaught exception becomes a generic 500
 *    with a Spring error blob, and the *HttpClient on the other end would
 *    surface something unreadable instead of the actual reason
 *    ("You are not the author of this post", "Community name is already
 *    taken.") that a console Command's consolePrinter can show directly.
 *
 * WHY ROUTES ARE FLAT RATHER THAN DEEPLY NESTED:
 * Single-resource actions (get/edit/delete a specific post, comment, etc.)
 * use flat paths like /api/posts/{postId} rather than
 * /api/communities/{communityId}/posts/{postId} -- the *Service methods
 * themselves only ever take the single id they need (PostService.findPostById
 * (postId) has no communityId parameter), so nesting the URL wouldn't reflect
 * anything the service actually checks. Community-scoped listing is the one
 * place nesting reflects real structure (GET /api/communities/{communityId}/posts),
 * since PostService.listPosts(communityId) is genuinely scoped that way.
 */
package com.app.controller;

import com.app.dto.LoginRequest;
import com.app.dto.UserDto;
import com.app.model.User;
import com.app.service.UserUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCases userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        // the validation (empty, email format, uniqueness) happens inside the UseCases
        // controller just unpacks the DTO
        User created = userService.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest request) {
        // sets UserUseCases's server-side loggedInUser
        User user = userService.login(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(toDto(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUser() {
        User user = userService.getLoggedInUser();
        if (user == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(toDto(user));
    }

    // manual mapping
    // should be deleted probably
    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDescription(user.getDescription());
        dto.setCreatedAt(user.getCreatedAt());
        // password deliberately not copied, it protects it
        return dto;
    }
}