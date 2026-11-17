
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.StrengthHistoryRecordEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StrengthHistoryRecordRepository
        extends JpaRepository<StrengthHistoryRecordEntity, Long> {

    // Find all records belonging to one snapshot.
    List<StrengthHistoryRecordEntity>
    findBySnapshotIdOrderByRecordIndexAsc(
            Long snapshotId
    );
}
