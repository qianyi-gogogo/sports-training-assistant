package com.qianyi.sportstrainingassistant.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailRequest(

        @NotBlank
        @Pattern(regexp = "^[0-9]{6}$")
        String code

) {}