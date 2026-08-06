package com.app.controller;

import com.app.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
//@RequestMapping("/")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> healthcheck() {
        return ApiResponse.ok(Map.of(
                "status", "UP",
                "message", "Api is working",
                "team", "Cookie Monsters"
        ));
    }

}