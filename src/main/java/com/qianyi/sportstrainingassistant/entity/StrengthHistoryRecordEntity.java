
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.PullUpMode;
import com.qianyi.sportstrainingassistant.model.PullUpRecord;
import com.qianyi.sportstrainingassistant.model.StrengthRecord;

import jakarta.persistence.*;

@Entity
@Table(
        name = "strength_history_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_strength_snapshot_position",
                        columnNames = {
                                "snapshot_id",
                                "record_index"
                        }
                )
        }
)
public class StrengthHistoryRecordEntity {

    // ==================== Identity ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "snapshot_id", nullable = false)
    private Long snapshotId;

    @Column(name = "record_index", nullable = false)
    private int recordIndex;

    // ==================== Strength Data ====================

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "reps", nullable = false)
    private int reps;

    @Column(name = "rir")
    private Integer rir;

    @Enumerated(EnumType.STRING)
    @Column(name = "pull_up_mode", length = 30)
    private PullUpMode pullUpMode;

    // ==================== Constructors ====================

    protected StrengthHistoryRecordEntity() {
        // Required by JPA.
    }

    // Ordinary strength exercise.
    public StrengthHistoryRecordEntity(
            Long snapshotId,
            int recordIndex,
            StrengthRecord record
    ) {
        validateIdentity(snapshotId, recordIndex);

        if (record == null) {
            throw new IllegalArgumentException(
                    "Strength record cannot be null"
            );
        }

        this.snapshotId = snapshotId;
        this.recordIndex = recordIndex;

        this.weightKg = record.getWeightKg();
        this.reps = record.getReps();
        this.rir = record.getRir();
        this.pullUpMode = null;
    }

    // Pull-up exercise.
    public StrengthHistoryRecordEntity(
            Long snapshotId,
            int recordIndex,
            PullUpRecord record
    ) {
        validateIdentity(snapshotId, recordIndex);

        if (record == null) {
            throw new IllegalArgumentException(
                    "Pull-up record cannot be null"
            );
        }

        this.snapshotId = snapshotId;
        this.recordIndex = recordIndex;

        this.weightKg = record.getWeightKg();
        this.reps = record.getReps();
        this.rir = record.getRir();
        this.pullUpMode = record.getMode();
    }

    // ==================== Validation ====================

    private void validateIdentity(
            Long snapshotId,
            int recordIndex
    ) {
        if (snapshotId == null || snapshotId <= 0) {
            throw new IllegalArgumentException(
                    "Snapshot ID must be positive"
            );
        }

        if (recordIndex < 0) {
            throw new IllegalArgumentException(
                    "Record index cannot be negative"
            );
        }
    }

    // ==================== Model Conversion ====================

    public StrengthRecord toStrengthRecord() {

        if (pullUpMode != null) {
            throw new IllegalStateException(
                    "This is a pull-up record"
            );
        }

        return new StrengthRecord(
                weightKg,
                reps,
                rir
        );
    }

    public PullUpRecord toPullUpRecord() {

        if (pullUpMode == null) {
            throw new IllegalStateException(
                    "This is not a pull-up record"
            );
        }

        return new PullUpRecord(
                pullUpMode,
                weightKg,
                reps,
                rir
        );
    }

    // ==================== Getters ====================

    public Long getId() {
        return id;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public int getReps() {
        return reps;
    }

    public Integer getRir() {
        return rir;
    }

    public PullUpMode getPullUpMode() {
        return pullUpMode;
    }
}
