
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.*;
import com.qianyi.sportstrainingassistant.model.*;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PullUpRecordRepositoryTest {

    @Autowired
    private PullUpRecordRepository repository;

    @Autowired
    private BodyProfileRepository bodyProfileRepository;

    @Autowired
    private EntityManager entityManager;


    // ==================== Test Data ====================

    private Long createBodyProfileId() {

        BasicBodyInfo basic = new BasicBodyInfo(
                175.0,
                70.0,
                21,
                BiologicalSex.MALE
        );

        BodyProfile profile = new BodyProfile(basic);

        Long userId = UUID.randomUUID()
                .getMostSignificantBits() & Long.MAX_VALUE;

        BodyProfileEntity saved =
                bodyProfileRepository.saveAndFlush(
                        new BodyProfileEntity(userId, profile)
                );

        return saved.getId();
    }


    // ==================== Save And Read ====================

    @Test
    void shouldSaveAndRestoreWeightedPullUp() {

        Long profileId = createBodyProfileId();

        PullUpRecord record = new PullUpRecord(
                PullUpMode.WEIGHTED,
                20.0,
                6,
                1
        );

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        profileId,
                        0,
                        record
                )
        );

        entityManager.clear();

        List<PullUpRecordEntity> results =
                repository.findByBodyProfileIdOrderByRecordIndexAsc(
                        profileId
                );

        assertEquals(1, results.size());

        PullUpRecord restored = results.get(0).toModel();

        assertEquals(PullUpMode.WEIGHTED, restored.getMode());
        assertEquals(20.0, restored.getWeightKg());
        assertEquals(6, restored.getReps());
        assertEquals(1, restored.getRir());
    }


    // ==================== Three Pull-Up Modes ====================

    @Test
    void shouldRestoreAllPullUpModes() {

        Long profileId = createBodyProfileId();

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        profileId,
                        0,
                        new PullUpRecord(
                                PullUpMode.BODYWEIGHT,
                                0.0,
                                12,
                                1
                        )
                )
        );

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        profileId,
                        1,
                        new PullUpRecord(
                                PullUpMode.WEIGHTED,
                                15.0,
                                6,
                                1
                        )
                )
        );

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        profileId,
                        2,
                        new PullUpRecord(
                                PullUpMode.ASSISTED,
                                20.0,
                                10,
                                2
                        )
                )
        );

        entityManager.clear();

        List<PullUpRecordEntity> results =
                repository.findByBodyProfileIdOrderByRecordIndexAsc(
                        profileId
                );

        assertEquals(3, results.size());

        assertEquals(
                PullUpMode.BODYWEIGHT,
                results.get(0).toModel().getMode()
        );

        assertEquals(
                PullUpMode.WEIGHTED,
                results.get(1).toModel().getMode()
        );

        assertEquals(
                PullUpMode.ASSISTED,
                results.get(2).toModel().getMode()
        );
    }


    // ==================== Record Order ====================

    @Test
    void shouldPreservePullUpRecordOrder() {

        Long profileId = createBodyProfileId();

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        profileId,
                        0,
                        new PullUpRecord(
                                PullUpMode.WEIGHTED,
                                25.0,
                                3,
                                1
                        )
                )
        );

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        profileId,
                        1,
                        new PullUpRecord(
                                PullUpMode.BODYWEIGHT,
                                0.0,
                                10,
                                2
                        )
                )
        );

        entityManager.clear();

        List<PullUpRecordEntity> results =
                repository.findByBodyProfileIdOrderByRecordIndexAsc(
                        profileId
                );

        assertEquals(2, results.size());

        assertEquals(0, results.get(0).getRecordIndex());
        assertEquals(1, results.get(1).getRecordIndex());

        assertEquals(
                25.0,
                results.get(0).toModel().getWeightKg()
        );

        assertEquals(
                0.0,
                results.get(1).toModel().getWeightKg()
        );
    }


    // ==================== Profile Isolation ====================

    @Test
    void shouldKeepDifferentProfilesSeparated() {

        Long firstProfileId = createBodyProfileId();
        Long secondProfileId = createBodyProfileId();

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        firstProfileId,
                        0,
                        new PullUpRecord(
                                PullUpMode.WEIGHTED,
                                10.0,
                                6,
                                1
                        )
                )
        );

        repository.saveAndFlush(
                new PullUpRecordEntity(
                        secondProfileId,
                        0,
                        new PullUpRecord(
                                PullUpMode.WEIGHTED,
                                30.0,
                                4,
                                1
                        )
                )
        );

        entityManager.clear();

        List<PullUpRecordEntity> results =
                repository.findByBodyProfileIdOrderByRecordIndexAsc(
                        firstProfileId
                );

        assertEquals(1, results.size());

        assertEquals(
                firstProfileId,
                results.get(0).getBodyProfileId()
        );

        assertEquals(
                10.0,
                results.get(0).toModel().getWeightKg()
        );
    }
}
