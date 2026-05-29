package com.restaurant.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    @Data
    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;
    }

    @Data
    public static class SignUpRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String name;

        @NotBlank
        private String fullname;

        @NotBlank
        private String password;

        private String phoneNumber;
        
        private String telegramChatId;
    }

    @Data
    public static class JwtAuthenticationResponse {
        private String accessToken;
        private String tokenType = "Bearer";
        private String role;
        private String name;

        public JwtAuthenticationResponse(String accessToken, String role, String name) {
            this.accessToken = accessToken;
            this.role = role;
            this.name = name;
        }
    }
}
