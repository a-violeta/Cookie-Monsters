package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.app.client.AuthTokenInterceptor;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /* HTTP Client Bean for client server communication over HTTP
    configured with timeouts and connection pooling for reliability
    AuthTokenInterceptor attaches the console's JWT (once logged in) to every
    outgoing request - without it, every write endpoint 403 post-login.*/

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, AuthTokenInterceptor authTokenInterceptor) {
        return builder
                .connectTimeout(java.time.Duration.ofSeconds(5))
                .readTimeout(java.time.Duration.ofSeconds(10))
                .additionalInterceptors(authTokenInterceptor)
                .build();
    }
}