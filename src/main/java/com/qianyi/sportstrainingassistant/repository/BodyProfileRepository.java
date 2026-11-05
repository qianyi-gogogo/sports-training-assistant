
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodyProfileRepository
        extends JpaRepository<BodyProfileEntity, Long> {

    // Find the current body profile by user ID.
    Optional<BodyProfileEntity> findByUserId(Long userId);

    // Check whether a user already has a body profile.
    boolean existsByUserId(Long userId);
}
