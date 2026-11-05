
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import com.qianyi.sportstrainingassistant.model.*;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BodyProfileRepositoryTest {

    @Autowired
    private BodyProfileRepository repository;

    @Autowired
    private EntityManager entityManager;


    // ==================== Test Data ====================

    private Long createTestUserId() {
        return UUID.randomUUID().getMostSignificantBits()
                & Long.MAX_VALUE;
    }

    private BodyProfile createBodyProfile() {

        BasicBodyInfo basic = new BasicBodyInfo(
                175.0,
                70.0,
                21,
                BiologicalSex.MALE
        );

        basic.setBodyFatPercentage(15.0);

        BodyMeasurements measurements =
                new BodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setWaistCircumferenceCm(78.0);
        measurements.setLeftArmCircumferenceCm(34.5);
        measurements.setRightArmCircumferenceCm(35.0);
        measurements.setThighCircumferenceCm(58.0);

        BodyProfile profile = new BodyProfile(basic);
        profile.setBodyMeasurements(measurements);

        return profile;
    }


    // ==================== Save ====================

    @Test
    void shouldSaveAndFindBodyProfile() {

        Long userId = createTestUserId();

        BodyProfileEntity entity =
                new BodyProfileEntity(
                        userId,
                        createBodyProfile()
                );

        repository.saveAndFlush(entity);

        // Clear the persistence context to force
        // the next query to read from the database.
        entityManager.clear();

        BodyProfileEntity saved = repository
                .findByUserId(userId)
                .orElseThrow();

        assertNotNull(saved.getId());

        BodyProfile restored = saved.toModel();

        assertEquals(
                175.0,
                restored.getBasicBodyInfo().getHeightCm()
        );

        assertEquals(
                70.0,
                restored.getBasicBodyInfo().getWeightKg()
        );

        assertEquals(
                15.0,
                restored.getBasicBodyInfo()
                        .getBodyFatPercentage()
        );

        assertEquals(
                34.5,
                restored.getBodyMeasurements()
                        .getLeftArmCircumferenceCm()
        );

        assertEquals(
                35.0,
                restored.getBodyMeasurements()
                        .getRightArmCircumferenceCm()
        );
    }


    // ==================== Update ====================

    @Test
    void shouldUpdateExistingBodyProfile() {

        Long userId = createTestUserId();

        BodyProfileEntity entity =
                repository.saveAndFlush(
                        new BodyProfileEntity(
                                userId,
                                createBodyProfile()
                        )
                );

        Long originalId = entity.getId();

        BodyProfile updatedProfile = createBodyProfile();

        updatedProfile.getBasicBodyInfo()
                .setWeightKg(72.5);

        updatedProfile.getBodyMeasurements()
                .setWaistCircumferenceCm(76.0);

        entity.updateFromModel(updatedProfile);

        repository.saveAndFlush(entity);

        entityManager.clear();

        BodyProfileEntity result = repository
                .findByUserId(userId)
                .orElseThrow();

        assertEquals(originalId, result.getId());

        assertEquals(
                72.5,
                result.toModel()
                        .getBasicBodyInfo()
                        .getWeightKg()
        );

        assertEquals(
                76.0,
                result.toModel()
                        .getBodyMeasurements()
                        .getWaistCircumferenceCm()
        );

        // Updating should not create another row.
        assertEquals(1L,
                repository.count() - countOtherProfiles(userId));
    }


    // ==================== Missing Profile ====================

    @Test
    void shouldReturnEmptyForMissingBodyProfile() {

        Long userId = createTestUserId();

        assertTrue(
                repository.findByUserId(userId).isEmpty()
        );

        assertFalse(
                repository.existsByUserId(userId)
        );
    }


    // ==================== Helper ====================

    private long countOtherProfiles(Long userId) {
        return entityManager.createQuery(
                        """
                        SELECT COUNT(p)
                        FROM BodyProfileEntity p
                        WHERE p.userId <> :userId
                        """,
                        Long.class
                )
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
