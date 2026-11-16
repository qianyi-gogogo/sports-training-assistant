
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
}
