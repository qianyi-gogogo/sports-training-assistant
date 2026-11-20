
package com.qianyi.sportstrainingassistant.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(

        // Username: 3-32 letters, numbers or underscores.
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z0-9_]{3,32}$",
                message = "Username must contain 3-32 letters, numbers or underscores"
        )
        String username,

        // Email is required for account recovery.
        @NotBlank
        @Email
        @Size(max = 254)
        String email,

        // Password will be hashed before persistence.
        @NotBlank
        @Size(min = 8, max = 72)
        String password

) {}
