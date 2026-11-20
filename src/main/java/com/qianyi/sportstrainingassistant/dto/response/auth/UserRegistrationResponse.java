
package com.qianyi.sportstrainingassistant.dto.response.auth;

public record UserRegistrationResponse(
        Long userId,
        String username,
        String email,
        boolean emailVerified
) {}
