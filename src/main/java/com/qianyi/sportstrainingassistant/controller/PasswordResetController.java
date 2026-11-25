package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.auth.PasswordResetConfirmRequest;
import com.qianyi.sportstrainingassistant.dto.request.auth.PasswordResetEmailRequest;
import com.qianyi.sportstrainingassistant.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/password-reset")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    // Do not expose whether an email belongs to a verified account.
    @PostMapping("/request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void request(@Valid @RequestBody PasswordResetEmailRequest request) {
        service.requestReset(request.email());
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@Valid @RequestBody PasswordResetConfirmRequest request) {
        var result = service.confirmReset(request.email(), request.code(), request.newPassword());
        return switch (result) {
            case SUCCESS -> ResponseEntity.noContent().build();
            case INVALID_OR_EXPIRED -> ResponseEntity.badRequest().body(Map.of(
                    "error", "验证码无效、已过期或已达到验证限制，请检查邮箱和验证码，必要时稍后重试。"));
            case SAME_PASSWORD -> ResponseEntity.badRequest().body(Map.of(
                    "error", "新密码不能与原密码相同。"));
        };
    }
}
