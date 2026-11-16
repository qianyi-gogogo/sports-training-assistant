
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.BodyProfileHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BodyProfileHistoryRepository
        extends JpaRepository<BodyProfileHistoryEntity, Long> {

    // Get complete history, oldest first.
    List<BodyProfileHistoryEntity>
    findByBodyProfileIdOrderByRecordedAtAscIdAsc(
            Long bodyProfileId
    );

    // Get the most recent record.
    Optional<BodyProfileHistoryEntity>
    findFirstByBodyProfileIdOrderByRecordedAtDescIdDesc(
            Long bodyProfileId
    );
}
