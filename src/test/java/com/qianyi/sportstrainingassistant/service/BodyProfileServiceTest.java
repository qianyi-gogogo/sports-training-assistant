
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.model.*;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BodyProfileServiceTest {

    @Autowired
    private BodyProfileService service;

    @Autowired
    private BodyProfileRepository repository;

    @Autowired
    private EntityManager entityManager;


    // ==================== Test Data ====================

    private Long createUserId() {
        return UUID.randomUUID()
                .getMostSignificantBits() & Long.MAX_VALUE;
    }

    private BodyProfile createProfile() {

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

        BodyProfile profile = new BodyProfile(basic);
        profile.setBodyMeasurements(measurements);

        return profile;
    }


    // ==================== Create ====================

    @Test
    void shouldCreateBodyProfile() {

        Long userId = createUserId();

        BodyProfile created =
                service.createProfile(userId, createProfile());

        assertEquals(
                175.0,
                created.getBasicBodyInfo().getHeightCm()
        );

        assertEquals(
                70.0,
                created.getBasicBodyInfo().getWeightKg()
        );

        assertTrue(repository.existsByUserId(userId));
    }


    // ==================== Read ====================

    @Test
    void shouldReadSavedBodyProfile() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        entityManager.clear();

        Optional<BodyProfile> result =
                service.getCurrentProfile(userId);

        assertTrue(result.isPresent());

        BodyProfile restored = result.orElseThrow();

        assertEquals(
                70.0,
                restored.getBasicBodyInfo().getWeightKg()
        );

        assertEquals(
                15.0,
                restored.getBasicBodyInfo().getBodyFatPercentage()
        );

        assertEquals(
                78.0,
                restored.getBodyMeasurements()
                        .getWaistCircumferenceCm()
        );
    }


    // ==================== Duplicate Create ====================

    @Test
    void shouldRejectDuplicateBodyProfile() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        assertThrows(
                IllegalStateException.class,
                () -> service.createProfile(
                        userId,
                        createProfile()
                )
        );
    }


    // ==================== Missing Profile ====================

    @Test
    void shouldReturnEmptyForMissingProfile() {

        Long userId = createUserId();

        Optional<BodyProfile> result =
                service.getCurrentProfile(userId);

        assertTrue(result.isEmpty());
    }

    // ==================== Strength Integration ====================

    @Test
    void shouldCreateProfileWithStrengthRecords() {

        Long userId = createUserId();

        BodyProfile profile = createProfile();

        StrengthProfile strength = new StrengthProfile();

        strength.setBenchPress(new ExerciseStrength(
                java.util.List.of(
                        new StrengthRecord(70.0, 5, 1),
                        new StrengthRecord(65.0, 8, 2)
                )
        ));

        profile.setStrengthProfile(strength);

        BodyProfile created =
                service.createProfile(userId, profile);

        assertNotNull(created.getStrengthProfile());

        assertEquals(
                2,
                created.getStrengthProfile()
                        .getBenchPress()
                        .getRecords()
                        .size()
        );

        entityManager.flush();
        entityManager.clear();

        BodyProfile restored = service
                .getCurrentProfile(userId)
                .orElseThrow();

        assertNotNull(restored.getStrengthProfile());

        assertEquals(
                70.0,
                restored.getStrengthProfile()
                        .getBenchPress()
                        .getRecords()
                        .get(0)
                        .getWeightKg()
        );

        assertEquals(
                65.0,
                restored.getStrengthProfile()
                        .getBenchPress()
                        .getRecords()
                        .get(1)
                        .getWeightKg()
        );
    }


    @Test
    void shouldAllowProfileWithoutStrengthRecords() {

        Long userId = createUserId();

        BodyProfile profile = createProfile();

        service.createProfile(userId, profile);

        entityManager.flush();
        entityManager.clear();

        BodyProfile restored = service
                .getCurrentProfile(userId)
                .orElseThrow();

        assertNull(restored.getStrengthProfile());

        assertEquals(
                175.0,
                restored.getBasicBodyInfo().getHeightCm()
        );
    }

}
