
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.PullUpRecordEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PullUpRecordRepository
        extends JpaRepository<PullUpRecordEntity, Long> {

    List<PullUpRecordEntity>
    findByBodyProfileIdOrderByRecordIndexAsc(
            Long bodyProfileId
    );

}
