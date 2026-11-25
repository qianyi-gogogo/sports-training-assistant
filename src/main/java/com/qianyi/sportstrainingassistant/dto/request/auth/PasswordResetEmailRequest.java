package com.qianyi.sportstrainingassistant.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetEmailRequest(
        @NotBlank @Email @Size(max = 254) String email
) { }
