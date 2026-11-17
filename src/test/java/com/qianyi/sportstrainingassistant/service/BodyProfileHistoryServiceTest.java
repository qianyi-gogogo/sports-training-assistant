
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.request.profile.BodyProfilePatchRequest;
import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import com.qianyi.sportstrainingassistant.entity.BodyProfileHistoryEntity;
import com.qianyi.sportstrainingassistant.model.*;
import com.qianyi.sportstrainingassistant.repository.BodyProfileHistoryRepository;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;

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
class BodyProfileHistoryServiceTest {

    @Autowired
    private BodyProfileService service;

    @Autowired
    private BodyProfileRepository profileRepository;

    @Autowired
    private BodyProfileHistoryRepository historyRepository;

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

        BodyMeasurements measurements =
                new BodyMeasurements();

        measurements.setWaistCircumferenceCm(78.0);

        BodyProfile profile = new BodyProfile(basic);
        profile.setBodyMeasurements(measurements);

        return profile;
    }

    private Long getProfileId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow()
                .getId();
    }

    private List<BodyProfileHistoryEntity> getHistory(
            Long bodyProfileId
    ) {
        entityManager.flush();
        entityManager.clear();

        return historyRepository
                .findByBodyProfileIdOrderByRecordedAtAscIdAsc(
                        bodyProfileId
                );
    }

    // ==================== Initial History ====================

    @Test
    void shouldSaveInitialHistory() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        Long profileId = getProfileId(userId);

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(1, history.size());

        assertEquals(70.0, history.get(0).getWeightKg());

        assertEquals(
                78.0,
                history.get(0).getWaistCircumferenceCm()
        );

        assertNotNull(history.get(0).getRecordedAt());
    }

    // ==================== PATCH History ====================

    @Test
    void shouldAppendHistoryAfterWeightUpdate() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        Long profileId = getProfileId(userId);

        BodyProfilePatchRequest request =
                new BodyProfilePatchRequest();

        request.setWeightKg(72.0);

        service.patchProfile(userId, request);

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(2, history.size());

        // Original history remains unchanged.
        assertEquals(70.0, history.get(0).getWeightKg());

        // New history contains updated weight.
        assertEquals(72.0, history.get(1).getWeightKg());

        // Unchanged waist is preserved.
        assertEquals(
                78.0,
                history.get(1).getWaistCircumferenceCm()
        );
    }

    // ==================== No Actual Change ====================

    @Test
    void shouldNotSaveDuplicateHistory() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        Long profileId = getProfileId(userId);

        BodyProfilePatchRequest request =
                new BodyProfilePatchRequest();

        // Same values as the initial profile.
        request.setWeightKg(70.0);
        request.setWaistCircumferenceCm(78.0);

        service.patchProfile(userId, request);

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(1, history.size());
    }

    // ==================== Invalid Update ====================

    @Test
    void shouldNotSaveHistoryForInvalidUpdate() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        Long profileId = getProfileId(userId);

        BodyProfilePatchRequest request =
                new BodyProfilePatchRequest();

        request.setWeightKg(500.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.patchProfile(userId, request)
        );

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(1, history.size());

        assertEquals(70.0, history.get(0).getWeightKg());

        BodyProfile restored =
                service.getCurrentProfile(userId)
                        .orElseThrow();

        assertEquals(
                70.0,
                restored.getBasicBodyInfo().getWeightKg()
        );
    }

    // ==================== Waist History ====================

    @Test
    void shouldAppendHistoryAfterWaistUpdate() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        Long profileId = getProfileId(userId);

        BodyProfilePatchRequest request =
                new BodyProfilePatchRequest();

        request.setWaistCircumferenceCm(80.0);

        service.patchProfile(userId, request);

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(2, history.size());

        // Initial historical record.
        assertEquals(70.0, history.get(0).getWeightKg());
        assertEquals(
                78.0,
                history.get(0).getWaistCircumferenceCm()
        );

        // Updated historical record.
        assertEquals(70.0, history.get(1).getWeightKg());
        assertEquals(
                80.0,
                history.get(1).getWaistCircumferenceCm()
        );
    }


    // ==================== History Consistency ====================

    @Test
    void shouldReturnHistoryConsistentWithCurrentProfile() {

        Long userId = createUserId();

        service.createProfile(userId, createProfile());

        BodyProfilePatchRequest first =
                new BodyProfilePatchRequest();

        first.setWeightKg(72.0);
        service.patchProfile(userId, first);

        BodyProfilePatchRequest second =
                new BodyProfilePatchRequest();

        second.setWaistCircumferenceCm(80.0);
        service.patchProfile(userId, second);

        entityManager.flush();
        entityManager.clear();

        // Read history using the actual Service method.
        var history = service.getProfileHistory(userId);

        assertEquals(3, history.size());

        assertEquals(70.0, history.get(0).weightKg());
        assertEquals(72.0, history.get(1).weightKg());
        assertEquals(72.0, history.get(2).weightKg());

        assertEquals(
                80.0,
                history.get(2).waistCircumferenceCm()
        );

        // Current profile must match the latest snapshot.
        BodyProfile current = service
                .getCurrentProfile(userId)
                .orElseThrow();

        assertEquals(
                current.getBasicBodyInfo().getWeightKg(),
                history.get(2).weightKg()
        );

        assertEquals(
                current.getBodyMeasurements()
                        .getWaistCircumferenceCm(),
                history.get(2).waistCircumferenceCm()
        );
    }

    // ==================== Complete Initial Snapshot ====================

    @Test
    void shouldSaveAllBodyMeasurementsInInitialHistory() {

        Long userId = createUserId();

        BodyProfile profile = createProfile();

        profile.getBasicBodyInfo()
                .setBodyFatPercentage(15.0);

        BodyMeasurements measurements =
                profile.getBodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setLeftArmCircumferenceCm(35.0);
        measurements.setRightArmCircumferenceCm(34.5);
        measurements.setThighCircumferenceCm(55.0);

        service.createProfile(userId, profile);

        Long profileId = getProfileId(userId);

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(1, history.size());

        BodyProfileHistoryEntity first = history.get(0);

        assertEquals(70.0, first.getWeightKg());
        assertEquals(15.0, first.getBodyFatPercentage());
        assertEquals(100.0, first.getChestCircumferenceCm());
        assertEquals(78.0, first.getWaistCircumferenceCm());
        assertEquals(35.0, first.getLeftArmCircumferenceCm());
        assertEquals(34.5, first.getRightArmCircumferenceCm());
        assertEquals(55.0, first.getThighCircumferenceCm());

        assertNotNull(first.getRecordedAt());
    }


    // ==================== Partial Update Snapshot ====================

    @Test
    void shouldPreserveOtherMeasurementsInNewHistory() {

        Long userId = createUserId();

        BodyProfile profile = createProfile();

        profile.getBasicBodyInfo()
                .setBodyFatPercentage(15.0);

        BodyMeasurements measurements =
                profile.getBodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setLeftArmCircumferenceCm(35.0);
        measurements.setRightArmCircumferenceCm(34.5);
        measurements.setThighCircumferenceCm(55.0);

        service.createProfile(userId, profile);

        Long profileId = getProfileId(userId);

        // Update only the left arm circumference.
        BodyProfilePatchRequest request =
                new BodyProfilePatchRequest();

        request.setLeftArmCircumferenceCm(36.0);

        service.patchProfile(userId, request);

        List<BodyProfileHistoryEntity> history =
                getHistory(profileId);

        assertEquals(2, history.size());

        BodyProfileHistoryEntity original = history.get(0);
        BodyProfileHistoryEntity updated = history.get(1);

        // The original snapshot must remain unchanged.
        assertEquals(
                35.0,
                original.getLeftArmCircumferenceCm()
        );

        // The updated snapshot contains the new value.
        assertEquals(
                36.0,
                updated.getLeftArmCircumferenceCm()
        );

        // All other measurements remain unchanged.
        assertEquals(70.0, updated.getWeightKg());
        assertEquals(15.0, updated.getBodyFatPercentage());
        assertEquals(100.0, updated.getChestCircumferenceCm());
        assertEquals(78.0, updated.getWaistCircumferenceCm());
        assertEquals(34.5, updated.getRightArmCircumferenceCm());
        assertEquals(55.0, updated.getThighCircumferenceCm());
    }

}
