package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.PasswordResetCodeEntity;
import com.qianyi.sportstrainingassistant.entity.UserEntity;
import com.qianyi.sportstrainingassistant.repository.PasswordResetCodeRepository;
import com.qianyi.sportstrainingassistant.repository.UserRepository;
import com.qianyi.sportstrainingassistant.security.PasswordCredentialStamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    private static final String EMAIL = "test@example.com";
    private static final String OLD = "OldPassword123";
    private static final String NEW = "NewPassword456";
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private UserRepository users;
    private PasswordResetCodeRepository codes;
    private EmailService mail;
    private UserEntity user;
    private PasswordResetService service;
    private final AtomicReference<PasswordResetCodeEntity> stored = new AtomicReference<>();
    private final AtomicReference<String> sent = new AtomicReference<>();

    @BeforeEach
    void setUp() {
        users = mock(UserRepository.class);
        codes = mock(PasswordResetCodeRepository.class);
        mail = mock(EmailService.class);
        user = new UserEntity("testuser", EMAIL, encoder.encode(OLD));
        ReflectionTestUtils.setField(user, "id", 1L);
        user.markEmailVerified();
        stored.set(null);
        sent.set(null);
        when(users.findByEmailForUpdate(EMAIL)).thenReturn(Optional.of(user));
        when(codes.findByUserId(1L)).thenAnswer(inv -> Optional.ofNullable(stored.get()));
        when(codes.saveAndFlush(any(PasswordResetCodeEntity.class))).thenAnswer(inv -> {
            PasswordResetCodeEntity entity = inv.getArgument(0);
            stored.set(entity);
            return entity;
        });
        when(codes.save(any(PasswordResetCodeEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        doAnswer(inv -> {
            String body = inv.getArgument(2);
            var matcher = Pattern.compile("Your password reset code is: ([0-9]{6})").matcher(body);
            assertTrue(matcher.find());
            sent.set(matcher.group(1));
            assertFalse(body.contains("http://"));
            return null;
        }).when(mail).sendEmail(eq(EMAIL), anyString(), anyString());
        service = new PasswordResetService(users, codes, mail, encoder);
    }

    private String sendCode() {
        service.requestReset(EMAIL);
        assertNotNull(sent.get());
        return sent.get();
    }

    private void passCooldown() {
        ReflectionTestUtils.setField(stored.get(), "createdAt", LocalDateTime.now().minusSeconds(61));
    }

    private static String wrongCode(String correct) {
        return correct.equals("000000") ? "111111" : "000000";
    }

    @Test
    void shouldSendCodeAndStoreOnlyHash() {
        String code = sendCode();
        assertNotEquals(code, stored.get().getCodeHash());
        assertTrue(encoder.matches(code + PasswordCredentialStamp.of(user.getPasswordHash()),
                stored.get().getCodeHash()));
    }

    @Test
    void shouldResetPasswordAndRejectReplay() {
        String code = sendCode();
        assertEquals(PasswordResetService.ResetResult.SUCCESS,
                service.confirmReset(EMAIL, code, NEW));
        assertTrue(encoder.matches(NEW, user.getPasswordHash()));
        assertFalse(encoder.matches(OLD, user.getPasswordHash()));
        assertTrue(stored.get().isExpired(LocalDateTime.now()));
        assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                service.confirmReset(EMAIL, code, "AnotherPassword123"));
    }

    @Test
    void shouldKeepFailedAttemptsWhenResending() {
        String first = sendCode();
        assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                service.confirmReset(EMAIL, wrongCode(first), NEW));
        verify(codes).save(stored.get());
        assertEquals(1, stored.get().getFailedAttempts());
        passCooldown();
        String second = sendCode();
        assertNotEquals(first, second);
        assertEquals(1, stored.get().getFailedAttempts());
        assertEquals(2, stored.get().getSendCount());
        assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                service.confirmReset(EMAIL, first, NEW));
    }

    @Test
    void shouldBlockAfterFiveFailedAttempts() {
        String code = sendCode();
        for (int i = 0; i < 5; i++) {
            assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                    service.confirmReset(EMAIL, wrongCode(code), NEW));
        }
        assertEquals(5, stored.get().getFailedAttempts());
        assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                service.confirmReset(EMAIL, code, NEW));
        passCooldown();
        service.requestReset(EMAIL);
        verify(mail, times(1)).sendEmail(eq(EMAIL), anyString(), anyString());
        assertTrue(encoder.matches(OLD, user.getPasswordHash()));
    }

    @Test
    void shouldBlockImmediateResendAndMoreThanFiveSends() {
        sendCode();
        service.requestReset(EMAIL);
        assertEquals(1, stored.get().getSendCount());
        for (int i = 1; i < 5; i++) {
            passCooldown();
            sendCode();
        }
        passCooldown();
        service.requestReset(EMAIL);
        assertEquals(5, stored.get().getSendCount());
        verify(mail, times(5)).sendEmail(eq(EMAIL), anyString(), anyString());
    }

    @Test
    void shouldRejectExpiredCode() {
        String code = sendCode();
        stored.get().invalidateCode(LocalDateTime.now().minusSeconds(1));
        assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                service.confirmReset(EMAIL, code, NEW));
    }

    @Test
    void shouldNotSendToUnknownOrUnverifiedEmail() {
        service.requestReset("unknown@example.com");
        var unverified = new UserEntity("unverified", EMAIL, encoder.encode(OLD));
        when(users.findByEmailForUpdate(EMAIL)).thenReturn(Optional.of(unverified));
        service.requestReset(EMAIL);
        verifyNoInteractions(mail, codes);
    }

    @Test
    void shouldRejectSamePasswordAndKeepCodeForCorrection() {
        String code = sendCode();
        assertEquals(PasswordResetService.ResetResult.SAME_PASSWORD,
                service.confirmReset(EMAIL, code, OLD));
        assertEquals(PasswordResetService.ResetResult.SUCCESS,
                service.confirmReset(EMAIL, code, NEW));
    }

    @Test
    void shouldRejectCodeIssuedBeforeAnotherPasswordChange() {
        String code = sendCode();
        user.changePasswordHash(encoder.encode("ChangedElsewhere123"));
        assertEquals(PasswordResetService.ResetResult.INVALID_OR_EXPIRED,
                service.confirmReset(EMAIL, code, NEW));
    }
}
