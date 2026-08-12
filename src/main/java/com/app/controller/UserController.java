package com.app.controller;

import com.app.dto.CommunityDto;
import com.app.dto.UserDto;
import com.app.mapper.CommunityMapper;
import com.app.model.Community;
import com.app.model.User;
import com.app.response.ApiResponse;
import com.app.service.CommunityAbstract;
import com.app.service.UserAbstract;
import com.app.service.AsyncLoggerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserAbstract userService;
    private final CommunityAbstract communityService;
    private final CommunityMapper communityMapper;
    private final AsyncLoggerService asyncLogger;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto dto) {
        User created = userService.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getDescription());

        asyncLogger.logInfo("New user account created via API: " + created.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(toDto(created)));
    }

    @GetMapping("/{userId}/communities")
    public ResponseEntity<ApiResponse<List<CommunityDto>>> listCommunitiesByUserId(@PathVariable Long userId) {
        List<Community> communities = communityService.listCommunitiesByUserId(userId);
        List<CommunityDto> dtos = communities.stream()
                .map(communityMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok(dtos, dtos.size()));
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDescription(user.getDescription());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}