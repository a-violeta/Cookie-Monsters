package com.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthRequests {

    @Data
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 20)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Alphanumeric and underscores only")
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 8)
        private String password;
    }

    @Data
    public static class LoginRequest {
        // field renamed from "username" to "identifier" for consistency with com.app.dto.LoginRequest (used by /api/users/login). Still only matches by
        // username server-side (see CustomUserDetailsService) - email login for this JWT flow is not implemented.
        @NotBlank
        private String username;//EDIT: rename identifier to username cuz API use just username

        @NotBlank
        private String password;
    }

    @Data
    public static class UpdateProfileRequest {
        private String displayName;

        @Pattern(regexp = "^(http|https)://.*$", message = "Must be a valid HTTP/HTTPS URL")
        private String avatarUrl;
    }

    @Data
    public static class ChangePasswordRequest {
        @NotBlank
        private String currentPassword;

        @NotBlank
        @Size(min = 8)
        private String newPassword;
    }
}