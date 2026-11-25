
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.UserEntity;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<UserEntity, Long> {

    // ==================== Duplicate Checks ====================

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // ==================== User Queries ====================

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    // ==================== Pessimistic Locking ====================

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmailForUpdate(
            @Param("email") String email
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id")
    Optional<UserEntity> findByIdForUpdate(
            @Param("id") Long id
    );

    // ==================== Registration Conflict Queries ====================

    @Query("""
            SELECT u.id
            FROM UserEntity u
            WHERE u.username = :username
               OR u.email = :email
            ORDER BY u.id
            """)
    List<Long> findConflictingUserIds(
            @Param("username") String username,
            @Param("email") String email
    );

    // ==================== Account Cleanup ====================

    @Query("""
            SELECT u.id
            FROM UserEntity u
            WHERE u.emailVerified = false
              AND u.email IS NOT NULL
              AND u.createdAt <= :cutoff
            ORDER BY u.id
            """)
    List<Long> findExpiredUnverifiedCandidateIds(
            @Param("cutoff") LocalDateTime cutoff
    );
    // ==================== Existing Business Data Checks ====================

    @Query("""
        SELECT COUNT(p)
        FROM BodyProfileEntity p
        WHERE p.userId = :userId
        """)
    long countBodyProfiles(
            @Param("userId") Long userId
    );

    @Query("""
        SELECT COUNT(g)
        FROM TrainingGoalEntity g
        WHERE g.userId = :userId
        """)
    long countTrainingGoals(
            @Param("userId") Long userId
    );
}
