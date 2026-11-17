
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.PullUpRecordEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PullUpRecordRepository
        extends JpaRepository<PullUpRecordEntity, Long> {

    // ==================== Read ====================

    List<PullUpRecordEntity>
    findByBodyProfileIdOrderByRecordIndexAsc(
            Long bodyProfileId
    );

    // ==================== Delete Current Records ====================

    @Modifying(flushAutomatically = true)
    @Query("""
            DELETE FROM PullUpRecordEntity r
            WHERE r.bodyProfileId = :bodyProfileId
            """)
    int deleteCurrentPullUpRecords(
            @Param("bodyProfileId") Long bodyProfileId
    );
}
