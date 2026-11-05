
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.StrengthRecord;

import jakarta.persistence.*;

@Entity
@Table(
        name = "exercise_strength_records",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_exercise_strength_position",
                columnNames = {
                        "body_profile_id",
                        "exercise_type",
                        "record_index"
                }
        )
)
public class ExerciseStrengthRecordEntity {

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
    private StrengthExerciseType exerciseType;

    @Column(name = "record_index", nullable = false)
    private int recordIndex;


    // ==================== Strength Record ====================

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "reps", nullable = false)
    private int reps;

    @Column(name = "rir")
    private Integer rir;


    // ==================== Constructors ====================

    protected ExerciseStrengthRecordEntity() {
        // Required by JPA.
    }

    public ExerciseStrengthRecordEntity(
            Long bodyProfileId,
            StrengthExerciseType exerciseType,
            int recordIndex,
            StrengthRecord record
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

        if (recordIndex < 0) {
            throw new IllegalArgumentException(
                    "Record index cannot be negative"
            );
        }

        if (record == null) {
            throw new IllegalArgumentException(
                    "Strength record cannot be null"
            );
        }

        this.bodyProfileId = bodyProfileId;
        this.exerciseType = exerciseType;
        this.recordIndex = recordIndex;

        this.weightKg = record.getWeightKg();
        this.reps = record.getReps();
        this.rir = record.getRir();
    }


    // ==================== Convert To Model ====================

    public StrengthRecord toModel() {

        return new StrengthRecord(
                weightKg,
                reps,
                rir
        );
    }


    // ==================== Getters ====================

    public Long getId() {
        return id;
    }

    public Long getBodyProfileId() {
        return bodyProfileId;
    }

    public StrengthExerciseType getExerciseType() {
        return exerciseType;
    }

    public int getRecordIndex() {
        return recordIndex;
    }
}
