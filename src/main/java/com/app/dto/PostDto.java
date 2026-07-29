package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for Post, used both as the HTTP request body (console -> server)
 * and the response body (server -> console).
 *
 * WHY FLATTENED TO IDs INSTEAD OF NESTING Community/User DIRECTLY:
 *
 * 1. Circular references would break JSON serialization.
 *    Community holds a list of Users, User holds a list of Posts, Post holds
 *    a Community and a User -- embedding the real entities here would start
 *    a loop trying to serialize Community -> User -> Post -> Community -> ...
 *
 * 2. It would leak data that should never cross the wire.
 *    A full User object includes the password field. Even with protections
 *    in place elsewhere, there's no reason a PostDto should ever be able to
 *    carry a password hash along for the ride.
 *
 * 3. The console can't rebuild real entities anyway.
 *    In "console" profile there is no datasource at all (see appllication-console.yml)
 *    CommunityRepository and UserRepository don't exist as beans there. So even if we wanted to hydrate
 *    a full Community/User instance client-side, we have no way to look one up.
 *    IDs (plus a couple of display fields) are all the client can meaningfully hold.
 *
 * WHAT HAPPENS ON EACH SIDE OF THE WIRE:
 *
 * - Server side (PostController): the *real* Community/User objects already exist
 *   as managed JPA entities, loaded via PostService's own repository lookups.
 *   PostMapper.toDto() just reads communityId/userId (and the display names) off
 *   those already-loaded associations -- no extra lookup needed, since PostService
 *   fetched them already.
 *
 * - Client side (PostHttpClient): the reverse direction. There is no "fromDto"
 *   mapper here on purpose -- rebuilding a real Post with a real Community/User
 *   would require the same repository lookups the server does, which the console
 *   can't perform. Instead, PostHttpClient builds a lightweight DETACHED Post --
 *   a Community/User with only id + name/username set, just enough for the
 *   console's print statements to show something meaningful. It is never saved,
 *   never re-queried, and should never be treated as a real managed entity.
 */

@Data
public class PostDto {
    private Long id;

    // For Post/Comment, the server always re-derives the real relationships from communityId/userId
    // via its own repositories rather than trusting a nested object the client sent
    // that's what keeps PostService.addPost's membership check meaningful instead of bypassable
    // Reference to the parent Community by id only
    @NotNull(message = "Community id is required")
    private Long communityId;

    @NotNull(message = "User id is required")
    private Long userId;

    // convenience fields for display, populated on responses only, ignored on requests if blank
    // so the console has something readable to print without a second lookup
    // if a client sends a create/update PostDto without these,
    // the server ignores them and derives the real values from communityId/userId
    private String communityName;
    private String username;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Text is required")
    private String text;

    private LocalDateTime createdAt;
}