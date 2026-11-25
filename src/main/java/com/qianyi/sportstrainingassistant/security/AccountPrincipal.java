package com.qianyi.sportstrainingassistant.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

// Retain a non-password stamp of the credentials used at authentication.
// Spring Security still erases the inherited password credentials after login.
public class AccountPrincipal extends User {

    private static final long serialVersionUID = 1L;
    private final String credentialStamp;

    public AccountPrincipal(String username, String passwordHash, boolean enabled) {
        super(username, passwordHash, enabled, true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.credentialStamp = PasswordCredentialStamp.of(passwordHash);
    }

    public boolean matchesCredentialStamp(String currentPasswordHash) {
        return credentialStamp.equals(PasswordCredentialStamp.of(currentPasswordHash));
    }
}
