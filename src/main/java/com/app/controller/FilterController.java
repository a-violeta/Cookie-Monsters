package com.app.controller;

import com.app.dto.FilterDto;
import com.app.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filters")
public class FilterController {
    @GetMapping
    public ResponseEntity<ApiResponse<List<FilterDto>>> getFilters() {
        List<FilterDto> filterDtos = List.of(
                new FilterDto(0, "none", "No filter"),
                new FilterDto(1, "grayscale", "Grayscale"),
                new FilterDto(2, "sepia", "Sepia"),
                new FilterDto(3, "invert", "Inverted")
        );
        return ResponseEntity.ok(ApiResponse.ok(filterDtos));
    }
}
