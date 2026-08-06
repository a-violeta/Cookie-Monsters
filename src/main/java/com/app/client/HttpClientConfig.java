package com.app.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * configuration properties for HTTP client connections to remote API servers.
 * properties are mapped from application.yml under 'app.http.client' prefix.
 *
 * Example configuration:
 * app:
 *   http:
 *     client:
 *       base-url: http://localhost:8081
 *       connection-timeout: 5000
 *       read-timeout: 10000
 */
@Component
@ConfigurationProperties(prefix = "app.http.client")
@Data
public class HttpClientConfig {
    private String baseUrl = "http://localhost:8080";
    private long connectionTimeout = 5000;
    private long readTimeout = 10000;
}