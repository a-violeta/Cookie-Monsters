package com.app.controller;

import com.app.dto.CommunityDto;
import com.app.dto.LoginRequest;
import com.app.dto.UserDto;
import com.app.mapper.CommunityMapper;
import com.app.model.Community;
import com.app.model.User;
import com.app.service.CommentUseCases;
import com.app.service.CommunityUseCases;
import com.app.service.UserUseCases;
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

    private final UserUseCases userService;
    private final CommunityUseCases communityService;
    private final CommunityMapper communityMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        // the validation happens inside the UseCases
        // controller just unpacks the DTO
        User created = userService.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest request) {
        // sets UseCases's loggedInUser
        User user = userService.login(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(toDto(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUser() {
        User user = userService.getLoggedInUser();
        if (user == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(toDto(user));
    }

    @GetMapping("/{userId}/communities")
    public List<CommunityDto> listCommunitiesByUserId(@PathVariable Long userId) {
        List<Community> communities = communityService.listCommunitiesByUserId(userId);
        return communities.stream()
                .map(communityMapper::toDto)
                .collect(Collectors.toList());
    }

    // manual mapping
    // should be deleted probably because we have mapping
    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDescription(user.getDescription());
        dto.setCreatedAt(user.getCreatedAt());
        // password deliberately not copied, it protects it
        return dto;
    }
}