
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import com.qianyi.sportstrainingassistant.model.*;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;
import com.qianyi.sportstrainingassistant.repository.ExerciseStrengthRecordRepository;
import com.qianyi.sportstrainingassistant.repository.PullUpRecordRepository;

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
class StrengthProfilePersistenceServiceTest {

    @Autowired
    private StrengthProfilePersistenceService service;

    @Autowired
    private BodyProfileRepository bodyProfileRepository;

    @Autowired
    private ExerciseStrengthRecordRepository exerciseRepository;

    @Autowired
    private PullUpRecordRepository pullUpRepository;

    @Autowired
    private EntityManager entityManager;


    // ==================== Test Data ====================

    private Long createBodyProfileId() {

        Long userId = UUID.randomUUID()
                .getMostSignificantBits() & Long.MAX_VALUE;

        BasicBodyInfo basic = new BasicBodyInfo(
                175.0,
                70.0,
                21,
                BiologicalSex.MALE
        );

        BodyProfile profile = new BodyProfile(basic);

        BodyProfileEntity saved = bodyProfileRepository
                .saveAndFlush(new BodyProfileEntity(userId, profile));

        return saved.getId();
    }

    private StrengthProfile createStrengthProfile() {

        StrengthProfile profile = new StrengthProfile();

        profile.setBenchPress(new ExerciseStrength(
                List.of(
                        new StrengthRecord(70.0, 5, 1),
                        new StrengthRecord(65.0, 8, 2),
                        new StrengthRecord(60.0, 10, 2)
                )
        ));

        profile.setPullUp(new PullUpStrength(
                List.of(
                        new PullUpRecord(
                                PullUpMode.BODYWEIGHT,
                                0.0,
                                12,
                                1
                        ),
                        new PullUpRecord(
                                PullUpMode.WEIGHTED,
                                10.0,
                                6,
                                2
                        )
                )
        ));

        return profile;
    }


    // ==================== Save ====================

    @Test
    void shouldSaveStrengthRecords() {

        Long bodyProfileId = createBodyProfileId();

        service.saveInitialStrength(
                bodyProfileId,
                createStrengthProfile()
        );

        exerciseRepository.flush();
        pullUpRepository.flush();

        assertEquals(
                3,
                exerciseRepository
                        .findByBodyProfileIdOrderByExerciseTypeAscRecordIndexAsc(
                                bodyProfileId
                        ).size()
        );

        assertEquals(
                2,
                pullUpRepository
                        .findByBodyProfileIdOrderByRecordIndexAsc(
                                bodyProfileId
                        ).size()
        );
    }


    // ==================== Read ====================

    @Test
    void shouldRestoreStrengthRecordsInOrder() {

        Long bodyProfileId = createBodyProfileId();

        service.saveInitialStrength(
                bodyProfileId,
                createStrengthProfile()
        );

        entityManager.flush();
        entityManager.clear();

        StrengthProfile restored =
                service.loadStrength(bodyProfileId);

        assertNotNull(restored.getBenchPress());

        List<StrengthRecord> records =
                restored.getBenchPress().getRecords();

        assertEquals(3, records.size());

        assertEquals(70.0, records.get(0).getWeightKg());
        assertEquals(5, records.get(0).getReps());
        assertEquals(1, records.get(0).getRir());

        assertEquals(65.0, records.get(1).getWeightKg());
        assertEquals(60.0, records.get(2).getWeightKg());

        assertNotNull(restored.getPullUp());

        List<PullUpRecord> pullUps =
                restored.getPullUp().getRecords();

        assertEquals(2, pullUps.size());

        assertEquals(
                PullUpMode.BODYWEIGHT,
                pullUps.get(0).getMode()
        );

        assertEquals(
                PullUpMode.WEIGHTED,
                pullUps.get(1).getMode()
        );

        assertEquals(10.0, pullUps.get(1).getWeightKg());
    }


    // ==================== Optional Data ====================

    @Test
    void shouldAllowMissingExercises() {

        Long bodyProfileId = createBodyProfileId();

        StrengthProfile profile = new StrengthProfile();

        profile.setSquat(new ExerciseStrength(
                List.of(new StrengthRecord(100.0, 5, null))
        ));

        service.saveInitialStrength(bodyProfileId, profile);

        entityManager.flush();
        entityManager.clear();

        StrengthProfile restored =
                service.loadStrength(bodyProfileId);

        assertNotNull(restored.getSquat());
        assertNull(restored.getBenchPress());
        assertNull(restored.getPullUp());

        assertNull(
                restored.getSquat()
                        .getRecords()
                        .get(0)
                        .getRir()
        );
    }


    // ==================== Duplicate Save ====================

    @Test
    void shouldRejectDuplicateInitialSave() {

        Long bodyProfileId = createBodyProfileId();

        StrengthProfile profile = createStrengthProfile();

        service.saveInitialStrength(bodyProfileId, profile);

        assertThrows(
                IllegalStateException.class,
                () -> service.saveInitialStrength(
                        bodyProfileId,
                        profile
                )
        );
    }


    // ==================== Invalid ID ====================

    @Test
    void shouldRejectInvalidBodyProfileId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.loadStrength(-1L)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.saveInitialStrength(
                        null,
                        createStrengthProfile()
                )
        );
    }
}

