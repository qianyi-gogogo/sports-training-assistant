
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<UserEntity, Long> {

    // Check whether the username is already registered.
    boolean existsByUsername(String username);

    // Check whether the email is already registered.
    boolean existsByEmail(String email);

    // Find a user by username for login.
    Optional<UserEntity> findByUsername(String username);

    // Find a user by email for account recovery.
    Optional<UserEntity> findByEmail(String email);
}
