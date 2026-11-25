
package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.auth.VerifyEmailRequest;
import com.qianyi.sportstrainingassistant.dto.response.auth.EmailVerificationResponse;
import com.qianyi.sportstrainingassistant.model.EmailVerificationStatus;
import com.qianyi.sportstrainingassistant.repository.UserRepository;
import com.qianyi.sportstrainingassistant.service.EmailVerificationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/email")
public class EmailVerificationController {

    private final EmailVerificationService service;
    private final UserRepository userRepository;

    // ==================== Constructor ====================

    public EmailVerificationController(
            EmailVerificationService service,
            UserRepository userRepository
    ) {
        this.service = service;
        this.userRepository = userRepository;
    }

    // ==================== Verification Session ====================

    @GetMapping("/session")
    public ResponseEntity<?> getVerificationSession(
            HttpSession session
    ) {

        Long userId = getVerificationUserId(session);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }

        var result = userRepository.findById(userId);

        if (result.isEmpty()
                || result.get().isEmailVerified()
                || result.get().getEmail() == null) {

            session.removeAttribute("verificationUserId");

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .body(Map.of(
                        "email", result.get().getEmail()
                ));
    }

    // ==================== Send Verification Code ====================

    @PostMapping("/send-code")
    public ResponseEntity<EmailVerificationResponse> sendCode(
            HttpSession session
    ) {

        Long userId = getVerificationUserId(session);

        if (userId == null) {
            return forbiddenResponse();
        }

        EmailVerificationResponse response =
                service.sendVerificationCode(userId);

        return buildHttpResponse(response);
    }

    // ==================== Verify Email ====================

    @PostMapping("/verify")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request,
            HttpSession session
    ) {

        Long userId = getVerificationUserId(session);

        if (userId == null) {
            return forbiddenResponse();
        }

        EmailVerificationResponse response =
                service.verifyCode(
                        userId,
                        request.code()
                );

        if (response.verified()) {

            session.removeAttribute(
                    "verificationUserId"
            );
        }

        return buildHttpResponse(response);
    }

    // ==================== Session Validation ====================

    private Long getVerificationUserId(
            HttpSession session
    ) {

        Object value = session.getAttribute(
                "verificationUserId"
        );

        if (value instanceof Long userId) {
            return userId;
        }

        return null;
    }

    // ==================== Forbidden Response ====================

    private ResponseEntity<EmailVerificationResponse>
    forbiddenResponse() {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        new EmailVerificationResponse(
                                EmailVerificationStatus.INVALID_OR_UNAVAILABLE,
                                false,
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                );
    }

    // ==================== HTTP Response Mapping ====================

    private ResponseEntity<EmailVerificationResponse>
    buildHttpResponse(
            EmailVerificationResponse response
    ) {

        return switch (response.status()) {

            case CODE_SENT, VERIFIED ->
                    ResponseEntity.ok(response);

            case RESEND_COOLDOWN,
                 SEND_LIMIT_REACHED,
                 ATTEMPT_LIMIT_REACHED ->

                    ResponseEntity
                            .status(HttpStatus.TOO_MANY_REQUESTS)
                            .body(response);

            case INVALID_CODE,
                 CODE_EXPIRED,
                 INVALID_OR_UNAVAILABLE ->

                    ResponseEntity
                            .badRequest()
                            .body(response);
        };
    }
}
