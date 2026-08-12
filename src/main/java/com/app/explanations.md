# `com.app.client` — HTTP Client Adapters

HTTP client adapters for the four `UseCases` ports (`User`, `Community`, `Post`, `Comment`).

## The full flow, end to end

1. A console `Command` holds a reference to a `*UseCases` interface (e.g. `PostUseCases`), never a concrete class. It doesn't know or care whether it's talking to a local service or a network client.

2. Spring decides **which** implementation gets injected based on `app.http.client.enabled`:
    - `false` (**server** profile): the real `*Service` classes are active.
    - `true` (**console** profile): these `*HttpClient` classes are active instead.

   Both sides carry a mirrored `@ConditionalOnProperty` so only one bean per interface ever exists in a given profile — never both, never neither.

3. A `*HttpClient` method does three things, always in this order:
    1. **Builds a request DTO** from the plain arguments it received (the same arguments the interface method always took — callers never notice a difference).
    2. **Calls `RestTemplate`** against `clientConfig.getBaseUrl()` + some path, wrapped in try/catch for `HttpClientErrorException` / `HttpServerErrorException`.
    3. **Converts whatever DTO comes back into a domain object** to return, since the interface's method signature promises a domain type (e.g. `PostUseCases.addPost` returns `Post`, not `PostDto`).

4. On the other end, the matching `*Controller` in `com.app.controller` receives the same DTO, unpacks its primitive fields, and calls the **real local `*Service`** — the one with the actual repositories, validation, and membership/ownership checks. The controller never trusts a nested entity graph from the client; it always re-derives relationships (`Community`, `User`, etc.) via the service's own lookups.

5. The controller maps the resulting entity back to a DTO (via a `*Mapper` where the mapping is pure field-copying, or a small manual method where a field like `password` needs to be excluded) and returns it as JSON.

6. Back in the `*HttpClient`, step 3.3 happens: the DTO becomes a domain object.

   **Important:** this object is a **detached, non-persisted** holder, not a real JPA entity. The console process has no datasource in `console` profile — there is no repository to hydrate a real `Community`/`User`/`Post`/`Comment` from. These reconstructed objects exist only so console `Command`s can call getters (`getUsername()`, `getCommunityName()`, etc.) to print something — they should never be passed back into a `*Service` expecting a managed entity, and they're never saved anywhere.

## Why exceptions are translated the way they are

A `GlobalExceptionHandler` (`@RestControllerAdvice`) on the server turns `IllegalArgumentException`/`IllegalStateException` into 400/409 responses with the original message as the body.

Each `*HttpClient` catches `HttpClientErrorException`/`HttpServerErrorException` and re-throws using `e.getResponseBodyAsString()` as the message — that string **is** the original service's error message, round-tripped through HTTP.

Without the exception handler, this would just be a generic Spring error blob instead of something a console `Command`'s `consolePrinter` can show meaningfully.

## The stack

### Console side:
```
CreatePostCommand.execute(args)
└─ postAbstract.addPost(communityId, userId, title, text)      <- Command only knows the interface
└─ PostHttpClient.addPost(...)                             <- Spring injected THIS impl (enabled=true)
├─ builds PostDto from the args
├─ restTemplate.postForObject(url, dto, PostDto.class)
│     │
│     │   ══════ HTTP request goes out over the network ══════
│     │
└─ (blocks here waiting for the HTTP response)
```
PostHttpClient has no idea a controller even exists — it just knows a URL and expects JSON back.

### Server side:
This is a completely separate stack, kicked off by Spring MVC/Tomcat receiving that HTTP request
```
DispatcherServlet (Tomcat, listening on :8081)
  └─ routes POST /api/posts to PostController.createPost(dto)     <- matched by @PostMapping
       ├─ @Valid runs first — bad input never reaches this method body
       └─ postService.addPost(dto.getCommunityId(), dto.getUserId(), dto.getTitle(), dto.getText())
            └─ PostService.addPost(...)                            <- Spring injected THIS impl (enabled=false)
                 ├─ communityRepository.findById(...)
                 ├─ userRepository.findById(...)
                 ├─ community.findUserById(userId) membership check
                 └─ postRepository.save(post)
       └─ postMapper.toDto(created)  →  PostDto
  └─ DispatcherServlet serializes PostDto to JSON, sends HTTP response back
```
### Putting both together:
```
[CONSOLE PROCESS]                          [SERVER PROCESS]
Command
  └─ PostUseCases.addPost(...)
       └─ PostHttpClient.addPost(...)
            └─ RestTemplate.postForObject ──HTTP──▶ DispatcherServlet
                                                        └─ PostController.createPost(dto)
                                                             └─ PostUseCases.addPost(...)
                                                                  └─ PostService.addPost(...)
                                                                       └─ (repositories, DB)
                                                             └─ postMapper.toDto(...)
            ◀──HTTP response (JSON)────────────────────────────────┘
       └─ toPost(response) → detached Post
  ← Command gets back a Post, prints it
```

### WHAT HAPPENS ON EACH SIDE OF THE WIRE:

**Server side (PostController in this example):**

The *real* Community/User objects already exist as managed JPA entities, loaded via PostService's own repository lookups.

PostMapper.toDto() just reads communityId/userId (and the display names) off those already-loaded associations -- no extra lookup needed, since PostService fetched them already.

**Client side (PostHttpClient):**

The reverse direction. There is no "fromDto" mapper here on purpose, rebuilding a real Post with a real Community/User would require the same repository lookups the server does, which the console can't perform

Instead, PostHttpClient builds a lightweight DETACHED Post, a Community/User with only id + name/username set, just enough for the console's print statements to show something meaningful. It is never saved, never re-queried, and should never be treated as a real managed entity.

## About the controllers

### The receiving half of the flow

REST controllers exposing the four UseCases ports over HTTP, for the console to call

1. A request DTO arrives, validated by @Valid against its @NotBlank/@NotNull/@Pattern annotations. Validation failures never reach a controller method body, they're caught by GlobalExceptionHandler, before any service is called.

2. THE CONTROLLER NEVER TRUSTS A NESTED ENTITY GRAPH FROM THE CLIENT. PostDto/CommentDto carry communityId/userId/postId as plain ids, not embedded objects, on purpose. The controller unpacks those ids and passes them as arguments straight to the real Service (PostService, CommentService, etc.), which does its OWN repository lookups and its OWN validation.

3. The Service method runs exactly the same logic it always has, whether called from console local mode or from a controller in server mode, the controller is a thin translation layer, not a second place where business rules live.

4. The resulting entity gets converted back to a response DTO, either using a mapper or a small manual method (in User: the password must never appear in the response).

5. If the service throws an exception, the controller method does NOT catch it. GlobalExceptionHandler catches it centrally and turns it into a 400 or 409 with the exception's message as the plain-text body. without it, an uncaught exception becomes a generic 500 with a Spring error blob, and the HttpClient on the other end would surface something unreadable instead of the actual reason ("You are not the author" etc) that consolePrinter can show directly.

### why routes are flat (but they don’t match the FE ones)

Single-resource actions (get/edit/delete a specific post, comment, etc.) use flat paths like /api/posts/{postId} rather than /api/communities/{communityId}/posts/{postId}

The Service methods themselves only ever take the single id they need (PostService.findPostById (postId) has no communityId parameter), so nesting the URL wouldn't reflect anything the service actually checks

Community-scoped listing is the one place nesting reflects real structure (GET /api/communities/{communityId}/posts), since PostService.listPosts(communityId) is genuinely scoped that way.