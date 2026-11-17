
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.BodyMeasurements;
import com.qianyi.sportstrainingassistant.model.BodyProfile;

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

    // ==================== Body Measurements ====================

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @Column(name = "chest_circumference_cm")
    private Double chestCircumferenceCm;

    @Column(name = "waist_circumference_cm")
    private Double waistCircumferenceCm;

    @Column(name = "left_arm_circumference_cm")
    private Double leftArmCircumferenceCm;

    @Column(name = "right_arm_circumference_cm")
    private Double rightArmCircumferenceCm;

    @Column(name = "thigh_circumference_cm")
    private Double thighCircumferenceCm;

    // ==================== Timestamp ====================

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // ==================== Constructors ====================

    protected BodyProfileHistoryEntity() {
        // Required by JPA.
    }

    public BodyProfileHistoryEntity(
            Long bodyProfileId,
            BodyProfile profile
    ) {
        if (bodyProfileId == null || bodyProfileId <= 0) {
            throw new IllegalArgumentException(
                    "Body profile ID must be positive"
            );
        }

        if (profile == null ||
                profile.getBasicBodyInfo() == null) {
            throw new IllegalArgumentException(
                    "Body profile cannot be null"
            );
        }

        this.bodyProfileId = bodyProfileId;

        // Reuse validated Model values.
        var basic = profile.getBasicBodyInfo();

        this.weightKg = basic.getWeightKg();
        this.bodyFatPercentage = basic.getBodyFatPercentage();

        BodyMeasurements measurements =
                profile.getBodyMeasurements();

        if (measurements != null) {
            this.chestCircumferenceCm =
                    measurements.getChestCircumferenceCm();

            this.waistCircumferenceCm =
                    measurements.getWaistCircumferenceCm();

            this.leftArmCircumferenceCm =
                    measurements.getLeftArmCircumferenceCm();

            this.rightArmCircumferenceCm =
                    measurements.getRightArmCircumferenceCm();

            this.thighCircumferenceCm =
                    measurements.getThighCircumferenceCm();
        }

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

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public Double getChestCircumferenceCm() {
        return chestCircumferenceCm;
    }

    public Double getWaistCircumferenceCm() {
        return waistCircumferenceCm;
    }

    public Double getLeftArmCircumferenceCm() {
        return leftArmCircumferenceCm;
    }

    public Double getRightArmCircumferenceCm() {
        return rightArmCircumferenceCm;
    }

    public Double getThighCircumferenceCm() {
        return thighCircumferenceCm;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
