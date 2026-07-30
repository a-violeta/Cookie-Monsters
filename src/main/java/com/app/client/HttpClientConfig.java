package com.app.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// configuration properties for HTTP client connections to remote API servers
// properties are mapped in application.yml under app.http.client

@Component
@ConfigurationProperties(prefix = "app.http.client")
@Data
public class HttpClientConfig {
    //private String baseUrl = "http://107.21.167.30:8081";
    private String baseUrl = "http://localhost:8081";
    private long connectionTimeout = 5000;
    private long readTimeout = 10000;
}