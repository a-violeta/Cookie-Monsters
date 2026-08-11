package com.app.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Always registered (harmless on the "server" profile, where nothing calls
// out through this RestTemplate) so Application.java's bean wiring stays simple.
@Component
@RequiredArgsConstructor
public class AuthTokenInterceptor implements ClientHttpRequestInterceptor {

    private final AuthTokenHolder authTokenHolder;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = authTokenHolder.getToken();
        if (token != null && !token.isBlank()) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return execution.execute(request, body);
    }
}
