package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // HTTP Client Bean for client server communication over HTTP
    // configured with timeouts and connection pooling for reliability

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(java.time.Duration.ofSeconds(5))
                .readTimeout(java.time.Duration.ofSeconds(10))
                .build();
    }
}