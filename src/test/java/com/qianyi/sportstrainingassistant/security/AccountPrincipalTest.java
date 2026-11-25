package com.qianyi.sportstrainingassistant.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

class AccountPrincipalTest {

    @Test
    void shouldKeepCredentialStampAfterPasswordErasure() {
        var encoder = new BCryptPasswordEncoder(4);
        String oldHash = encoder.encode("OldPassword123");
        String newHash = encoder.encode("NewPassword456");
        var principal = new AccountPrincipal("testuser", oldHash, true);
        principal.eraseCredentials();
        assertNull(principal.getPassword());
        assertTrue(principal.matchesCredentialStamp(oldHash));
        assertFalse(principal.matchesCredentialStamp(newHash));
    }
}
