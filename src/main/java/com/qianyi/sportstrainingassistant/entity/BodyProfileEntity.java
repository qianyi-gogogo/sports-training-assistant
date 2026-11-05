
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.BasicBodyInfo;
import com.qianyi.sportstrainingassistant.model.BiologicalSex;
import com.qianyi.sportstrainingassistant.model.BodyMeasurements;
import com.qianyi.sportstrainingassistant.model.BodyProfile;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "body_profiles",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_body_profile_user",
                columnNames = "user_id"
        )
)
public class BodyProfileEntity {

    // ==================== Identity ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    // ==================== Basic Body Info ====================

    @Column(name = "height_cm", nullable = false)
    private double heightCm;

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "biological_sex", nullable = false, length = 20)
    private BiologicalSex biologicalSex;


    // ==================== Body Measurements ====================

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


    // ==================== Timestamps ====================

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // ==================== Constructors ====================

    protected BodyProfileEntity() {
        // Required by JPA.
    }

    public BodyProfileEntity(Long userId, BodyProfile profile) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive"
            );
        }

        this.userId = userId;
        updateFromModel(profile);
    }


    // ==================== Update Current Profile ====================

    public void updateFromModel(BodyProfile profile) {

        if (profile == null) {
            throw new IllegalArgumentException(
                    "Body profile cannot be null"
            );
        }

        BasicBodyInfo basic = profile.getBasicBodyInfo();

        if (basic == null) {
            throw new IllegalArgumentException(
                    "Basic body info cannot be null"
            );
        }

        // Convert existing validated Model objects into
        // fresh validated snapshots before updating the Entity.
        BasicBodyInfo validatedBasic = new BasicBodyInfo(
                basic.getHeightCm(),
                basic.getWeightKg(),
                basic.getAge(),
                basic.getBiologicalSex()
        );

        validatedBasic.setBodyFatPercentage(
                basic.getBodyFatPercentage()
        );

        BodyMeasurements validatedMeasurements =
                new BodyMeasurements();

        BodyMeasurements measurements =
                profile.getBodyMeasurements();

        if (measurements != null) {

            validatedMeasurements.setChestCircumferenceCm(
                    measurements.getChestCircumferenceCm()
            );

            validatedMeasurements.setWaistCircumferenceCm(
                    measurements.getWaistCircumferenceCm()
            );

            validatedMeasurements.setLeftArmCircumferenceCm(
                    measurements.getLeftArmCircumferenceCm()
            );

            validatedMeasurements.setRightArmCircumferenceCm(
                    measurements.getRightArmCircumferenceCm()
            );

            validatedMeasurements.setThighCircumferenceCm(
                    measurements.getThighCircumferenceCm()
            );
        }

        // Apply values only after validation succeeds.
        this.heightCm = validatedBasic.getHeightCm();
        this.weightKg = validatedBasic.getWeightKg();
        this.age = validatedBasic.getAge();
        this.biologicalSex = validatedBasic.getBiologicalSex();
        this.bodyFatPercentage =
                validatedBasic.getBodyFatPercentage();

        this.chestCircumferenceCm =
                validatedMeasurements.getChestCircumferenceCm();

        this.waistCircumferenceCm =
                validatedMeasurements.getWaistCircumferenceCm();

        this.leftArmCircumferenceCm =
                validatedMeasurements.getLeftArmCircumferenceCm();

        this.rightArmCircumferenceCm =
                validatedMeasurements.getRightArmCircumferenceCm();

        this.thighCircumferenceCm =
                validatedMeasurements.getThighCircumferenceCm();
    }


    // ==================== Convert To Model ====================

    public BodyProfile toModel() {

        BasicBodyInfo basic = new BasicBodyInfo(
                heightCm,
                weightKg,
                age,
                biologicalSex
        );

        basic.setBodyFatPercentage(bodyFatPercentage);

        BodyProfile profile = new BodyProfile(basic);

        BodyMeasurements measurements =
                new BodyMeasurements();

        measurements.setChestCircumferenceCm(
                chestCircumferenceCm
        );

        measurements.setWaistCircumferenceCm(
                waistCircumferenceCm
        );

        measurements.setLeftArmCircumferenceCm(
                leftArmCircumferenceCm
        );

        measurements.setRightArmCircumferenceCm(
                rightArmCircumferenceCm
        );

        measurements.setThighCircumferenceCm(
                thighCircumferenceCm
        );

        profile.setBodyMeasurements(measurements);

        // StrengthProfile will be mapped in the next phase.
        return profile;
    }


    // ==================== Getters ====================

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
