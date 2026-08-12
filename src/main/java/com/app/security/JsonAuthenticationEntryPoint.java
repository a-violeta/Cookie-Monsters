package com.app.security;

import com.app.response.ApiError;
import com.app.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Fires when a protected endpoint is hit with no JWT, or an invalid/expired one.
 * Without this, Spring Security's default handling kicks in - which does NOT
 * match the documented {success:false, error:{...}, timestamp, path} error shape
 * that GlobalExceptionHandler produces for every other error case.
 */
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ApiResponse<Void> body = new ApiResponse<>();
        body.setSuccess(false);
        body.setError(new ApiError("UNAUTHORIZED", "Authentication required", null));
        body.setTimestamp(Instant.now().toString());
        body.setPath(request.getRequestURI());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
