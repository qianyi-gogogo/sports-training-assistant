package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.auth.PasswordResetEmailRequest;
import com.qianyi.sportstrainingassistant.dto.request.auth.PasswordResetConfirmRequest;
import com.qianyi.sportstrainingassistant.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password-reset")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    // Always return the same status, even if the email is not registered.
    @PostMapping("/request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void request(@Valid @RequestBody PasswordResetEmailRequest request) {
        service.requestReset(request.email());
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@Valid @RequestBody PasswordResetConfirmRequest request) {
        service.confirmReset(request.token(), request.newPassword());
    }
}
