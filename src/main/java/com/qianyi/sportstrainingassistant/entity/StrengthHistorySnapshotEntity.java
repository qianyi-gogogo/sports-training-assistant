
package com.qianyi.sportstrainingassistant.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "strength_history_snapshots",
        indexes = {
                @Index(
                        name = "idx_strength_history_profile_time",
                        columnList = "body_profile_id, recorded_at"
                )
        }
)
public class StrengthHistorySnapshotEntity {

    // ==================== Identity ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "body_profile_id", nullable = false)
    private Long bodyProfileId;

    // ==================== Exercise ====================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "exercise_type",
            nullable = false,
            length = 50
    )
    private StrengthHistoryExerciseType exerciseType;

    // ==================== Timestamp ====================

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // ==================== Constructors ====================

    protected StrengthHistorySnapshotEntity() {
        // Required by JPA.
    }

    public StrengthHistorySnapshotEntity(
            Long bodyProfileId,
            StrengthHistoryExerciseType exerciseType
    ) {
        if (bodyProfileId == null || bodyProfileId <= 0) {
            throw new IllegalArgumentException(
                    "Body profile ID must be positive"
            );
        }

        if (exerciseType == null) {
            throw new IllegalArgumentException(
                    "Exercise type cannot be null"
            );
        }

        this.bodyProfileId = bodyProfileId;
        this.exerciseType = exerciseType;
        this.recordedAt = LocalDateTime.now();
    }

    // ==================== Getters ====================

    public Long getId() {
        return id;
    }

    public Long getBodyProfileId() {
        return bodyProfileId;
    }

    public StrengthHistoryExerciseType getExerciseType() {
        return exerciseType;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
