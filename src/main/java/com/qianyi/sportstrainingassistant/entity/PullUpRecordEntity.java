
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.PullUpMode;
import com.qianyi.sportstrainingassistant.model.PullUpRecord;

import jakarta.persistence.*;

@Entity
@Table(name = "pull_up_records")
public class PullUpRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "body_profile_id", nullable = false)
    private Long bodyProfileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private PullUpMode mode;

    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @Column(name = "reps", nullable = false)
    private Integer reps;

    @Column(name = "rir")
    private Integer rir;

    @Column(name = "record_index", nullable = false)
    private Integer recordIndex;

    protected PullUpRecordEntity() {
        // Required by JPA
    }

    public PullUpRecordEntity(
            Long bodyProfileId,
            int recordIndex,
            PullUpRecord record
    ) {
        if (bodyProfileId == null || bodyProfileId <= 0) {
            throw new IllegalArgumentException(
                    "Body profile ID must be positive"
            );
        }

        if (recordIndex < 0) {
            throw new IllegalArgumentException(
                    "Record index cannot be negative"
            );
        }

        if (record == null) {
            throw new IllegalArgumentException(
                    "Pull-up record cannot be null"
            );
        }

        this.bodyProfileId = bodyProfileId;
        this.recordIndex = recordIndex;
        this.mode = record.getMode();
        this.weightKg = record.getWeightKg();
        this.reps = record.getReps();
        this.rir = record.getRir();
    }

    public PullUpRecord toModel() {
        return new PullUpRecord(
                mode,
                weightKg,
                reps,
                rir
        );
    }

    public Long getId() {
        return id;
    }

    public Long getBodyProfileId() {
        return bodyProfileId;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public PullUpMode getMode() {
        return mode;
    }
}
