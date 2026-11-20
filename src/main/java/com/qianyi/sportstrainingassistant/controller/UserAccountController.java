package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.auth.UserRegistrationRequest;
import com.qianyi.sportstrainingassistant.dto.response.auth.UserRegistrationResponse;
import com.qianyi.sportstrainingassistant.service.UserAccountService;

import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // ==================== Register ====================

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponse register(
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        return userAccountService.register(request);
    }

    // ==================== Current User ====================

    @GetMapping("/me")
    public UserRegistrationResponse getCurrentUser(
            Authentication authentication
    ) {
        return userAccountService.getCurrentUser(
                authentication.getName()
        );
    }

    // ==================== CSRF Token ====================

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> getCsrfToken(
            CsrfToken csrfToken
    ) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(Map.of(
                        "headerName", csrfToken.getHeaderName(),
                        "token", csrfToken.getToken()
                ));
    }
}
