package com.app.security;

import com.app.response.ApiError;
import com.app.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/*
    Fires when a request is authenticated but not allowed (e.g. Spring Security-level
    authorization failures). Same rationale as JsonAuthenticationEntryPoint - keeps the
    response shape consistent with GlobalExceptionHandler instead of Spring Security's default.
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ApiResponse<Void> body = new ApiResponse<>();
        body.setSuccess(false);
        body.setError(new ApiError("FORBIDDEN", "Access to this resource is forbidden", null));
        body.setTimestamp(Instant.now().toString());
        body.setPath(request.getRequestURI());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
