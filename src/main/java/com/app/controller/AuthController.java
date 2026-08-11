package com.app.controller;

import com.app.response.ApiResponse;
import com.app.dto.AuthRequests.*;
import com.app.dto.AuthResponseDto;
import com.app.model.User;
import com.app.security.JwtUtil;
import com.app.service.AsyncLoggerService;
import com.app.service.UserUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserUseCases userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AsyncLoggerService asyncLogger;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

        String token = jwtUtil.generateToken(user.getUsername());
        asyncLogger.logInfo("New user registered: " + user.getUsername());

        return ResponseEntity.ok(ApiResponse.ok(new AuthResponseDto(token, mapToUserDetails(user))));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userService.findByUsername(request.getUsername());
        String token = jwtUtil.generateToken(user.getUsername());

        asyncLogger.logInfo("User logged in: " + user.getUsername());

        return ResponseEntity.ok(ApiResponse.ok(new AuthResponseDto(token, mapToUserDetails(user))));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponseDto.UserDetails>> getMe(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(mapToUserDetails(user)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponseDto.UserDetails>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        User updatedUser = userService.updateProfile(
                authentication.getName(),
                request.getDisplayName(),
                request.getAvatarUrl()
        );

        asyncLogger.logInfo("User updated profile: " + updatedUser.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(mapToUserDetails(updatedUser)));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        userService.changePassword(
                authentication.getName(),
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        asyncLogger.logInfo("User changed password: " + authentication.getName());
        return ResponseEntity.ok(ApiResponse.message("Password changed successfully"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@Valid @RequestBody DeleteAccountRequest request, Authentication authentication) {
        userService.deleteAccount(authentication.getName(), request.getPassword());
        asyncLogger.logInfo("User deleted their account: " + authentication.getName());
        return ResponseEntity.ok(ApiResponse.message("Account deleted successfully"));
    }

    private AuthResponseDto.UserDetails mapToUserDetails(User user) {
        return new AuthResponseDto.UserDetails(
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getAvatarUrl()
        );
    }
}