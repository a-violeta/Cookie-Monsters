package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private UserDetails user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDetails {
        private String username;
        private String email;
        private String displayName;
        private String avatarUrl;
    }
}