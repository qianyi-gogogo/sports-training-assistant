
package com.qianyi.sportstrainingassistant.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "body_profile_history",
        indexes = {
                @Index(
                        name = "idx_body_history_profile_time",
                        columnList = "body_profile_id, recorded_at"
                )
        }
)
public class BodyProfileHistoryEntity {

    // ==================== Identity ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "body_profile_id", nullable = false)
    private Long bodyProfileId;

    // ==================== Historical Data ====================

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "waist_circumference_cm")
    private Double waistCircumferenceCm;

    // ==================== Timestamp ====================

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // ==================== Constructors ====================

    protected BodyProfileHistoryEntity() {
        // Required by JPA.
    }

    public BodyProfileHistoryEntity(
            Long bodyProfileId,
            double weightKg,
            Double waistCircumferenceCm
    ) {
        if (bodyProfileId == null || bodyProfileId <= 0) {
            throw new IllegalArgumentException(
                    "Body profile ID must be positive"
            );
        }

        if (!Double.isFinite(weightKg)
                || weightKg < 10
                || weightKg > 300) {
            throw new IllegalArgumentException(
                    "Invalid body weight"
            );
        }

        if (waistCircumferenceCm != null
                && (!Double.isFinite(waistCircumferenceCm)
                || waistCircumferenceCm < 30
                || waistCircumferenceCm > 200)) {
            throw new IllegalArgumentException(
                    "Invalid waist circumference"
            );
        }

        this.bodyProfileId = bodyProfileId;
        this.weightKg = weightKg;
        this.waistCircumferenceCm = waistCircumferenceCm;
        this.recordedAt = LocalDateTime.now();
    }

    // ==================== Getters ====================

    public Long getId() {
        return id;
    }

    public Long getBodyProfileId() {
        return bodyProfileId;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public Double getWaistCircumferenceCm() {
        return waistCircumferenceCm;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
