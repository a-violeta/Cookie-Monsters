package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// this is also a DTO
// login uses a name/email and a password, not a whole User
// because on UserDto description is mandatory, login needs a custom DTO

@Data
public class LoginRequest {
    //@NotBlank(message = "Username or email is required") - we did a login with username OR email but at api is just username
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}