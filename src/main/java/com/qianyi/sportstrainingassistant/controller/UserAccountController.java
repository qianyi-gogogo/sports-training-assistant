package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.auth.UserRegistrationRequest;
import com.qianyi.sportstrainingassistant.dto.request.auth.ChangePasswordRequest;
import com.qianyi.sportstrainingassistant.dto.response.auth.UserRegistrationResponse;
import com.qianyi.sportstrainingassistant.service.UserAccountService;

import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            @Valid @RequestBody UserRegistrationRequest request,
            HttpServletRequest httpRequest
    ) {

        UserRegistrationResponse response =
                userAccountService.register(request);

        // ==================== Verification Session ====================

        var session = httpRequest.getSession(true);

// Rotate the session ID for security.
        httpRequest.changeSessionId();

// Bind the verification session to the registered user.
        session.setAttribute(
                "verificationUserId",
                response.userId()
        );

        return response;
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

    // ==================== Change Password ====================

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        userAccountService.changePassword(
                authentication.getName(),
                request.currentPassword(),
                request.newPassword()
        );

        // Immediately log out the current session after a successful change.
        new SecurityContextLogoutHandler().logout(
                httpRequest, httpResponse, authentication
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
