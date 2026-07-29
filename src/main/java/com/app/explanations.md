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
└─ postUseCases.addPost(communityId, userId, title, text)      <- Command only knows the interface
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