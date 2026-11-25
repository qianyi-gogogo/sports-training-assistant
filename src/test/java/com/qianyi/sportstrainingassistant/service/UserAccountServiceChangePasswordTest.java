package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.UserEntity;
import com.qianyi.sportstrainingassistant.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccountServiceChangePasswordTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void shouldChangePasswordWhenCurrentPasswordMatches() {
        UserRepository repository = mock(UserRepository.class);
        UserEntity user = new UserEntity(
                "testuser1", "testuser1@example.com", encoder.encode("OldPassword123")
        );
        when(repository.findByUsername("testuser1"))
                .thenReturn(Optional.of(user));

        new UserAccountService(
                repository,
                mock(AccountCleanupService.class)
        )
                .changePassword("testuser1", "OldPassword123", "NewPassword456");

        assertTrue(encoder.matches("NewPassword456", user.getPasswordHash()));
        assertFalse(encoder.matches("OldPassword123", user.getPasswordHash()));
        verify(repository).save(user);
    }

    @Test
    void shouldRejectIncorrectCurrentPasswordWithoutSaving() {
        UserRepository repository = mock(UserRepository.class);
        String oldHash = encoder.encode("OldPassword123");
        UserEntity user = new UserEntity(
                "testuser1", "testuser1@example.com", oldHash
        );
        when(repository.findByUsername("testuser1"))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
                new UserAccountService(
                        repository,
                        mock(AccountCleanupService.class)
                )
                        .changePassword("testuser1", "WrongPassword", "NewPassword456")
        );
        assertEquals(oldHash, user.getPasswordHash());
        verify(repository, never()).save(any(UserEntity.class));
    }

    @Test
    void shouldRejectReusingCurrentPassword() {
        UserRepository repository = mock(UserRepository.class);
        String hash = encoder.encode("OldPassword123");
        UserEntity user = new UserEntity("testuser1", "testuser1@example.com", hash);
        when(repository.findByUsername("testuser1"))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
                new UserAccountService(
                        repository,
                        mock(AccountCleanupService.class)
                )
                        .changePassword("testuser1", "OldPassword123", "OldPassword123")
        );
        assertEquals(hash, user.getPasswordHash());
        verify(repository, never()).save(any(UserEntity.class));
    }

    @Test
    void shouldRejectPasswordExceeding72Utf8Bytes() {
        UserRepository repository = mock(UserRepository.class);
        UserEntity user = new UserEntity(
                "testuser1", "testuser1@example.com", encoder.encode("OldPassword123")
        );
        when(repository.findByUsername("testuser1"))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
                new UserAccountService(
                        repository,
                        mock(AccountCleanupService.class)
                )
                        .changePassword("testuser1", "OldPassword123", "密码".repeat(25))
        );
        verify(repository, never()).save(any(UserEntity.class));
    }
}
