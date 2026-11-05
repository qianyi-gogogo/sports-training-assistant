
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingGoalRepository
        extends JpaRepository<TrainingGoalEntity, Long> {

    // Find the newest training goal version.
    Optional<TrainingGoalEntity>
    findFirstByUserIdOrderByVersionDesc(Long userId);

    // Find every version, newest first.
    List<TrainingGoalEntity>
    findByUserIdOrderByVersionDesc(Long userId);

    // Check whether the user has any training goal.
    boolean existsByUserId(Long userId);

    // Retained for compatibility with existing tests.
    Optional<TrainingGoalEntity> findByUserId(Long userId);
}
