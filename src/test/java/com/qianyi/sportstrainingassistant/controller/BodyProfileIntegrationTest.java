
package com.qianyi.sportstrainingassistant.controller;

import jakarta.persistence.EntityManager;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;
import com.qianyi.sportstrainingassistant.repository.StrengthHistorySnapshotRepository;
import com.qianyi.sportstrainingassistant.entity.StrengthHistoryExerciseType;
import com.qianyi.sportstrainingassistant.repository.StrengthHistoryRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BodyProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BodyProfileRepository profileRepository;

    @Autowired
    private StrengthHistorySnapshotRepository snapshotRepository;

    @Autowired
    private StrengthHistoryRecordRepository historyRecordRepository;

    // ==================== Test Data ====================

    private Long createUserId() {
        return UUID.randomUUID()
                .getMostSignificantBits() & Long.MAX_VALUE;
    }

    private String validJson() {
        return """
                {
                  "basicBodyInfo": {
                    "heightCm": 175.0,
                    "weightKg": 70.0,
                    "age": 21,
                    "biologicalSex": "MALE",
                    "bodyFatPercentage": 15.0
                  },
                  "bodyMeasurements": {
                    "chestCircumferenceCm": 100.0,
                    "waistCircumferenceCm": 78.0,
                    "leftArmCircumferenceCm": 35.0,
                    "rightArmCircumferenceCm": 34.5,
                    "thighCircumferenceCm": 55.0
                  },
                  "strengthProfile": {
                    "benchPress": [
                      {
                        "weightKg": 70.0,
                        "reps": 5,
                        "rir": 1
                      },
                      {
                        "weightKg": 60.0,
                        "reps": 10,
                        "rir": 2
                      }
                    ],
                    "pullUp": [
                      {
                        "mode": "BODYWEIGHT",
                        "weightKg": 0.0,
                        "reps": 12,
                        "rir": 1
                      }
                    ]
                  }
                }
                """;
    }

    // ==================== Create & Read ====================

    @Test
    void shouldCreateAndReadCompleteProfile()
            throws Exception {

        Long userId = createUserId();

        String url = "/api/users/" + userId + "/profile";

        // 1. Create profile through HTTP.
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.basicBodyInfo.weightKg")
                        .value(70.0));

        // 2. Synchronize and clear persistence context.
        entityManager.flush();
        entityManager.clear();

        // 3. Read through HTTP.
        mockMvc.perform(
                        get(url + "/current")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basicBodyInfo.heightCm")
                        .value(175.0))
                .andExpect(jsonPath(
                        "$.bodyMeasurements.leftArmCircumferenceCm"
                ).value(35.0))
                .andExpect(jsonPath(
                        "$.bodyMeasurements.rightArmCircumferenceCm"
                ).value(34.5))
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(70.0))
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[1].reps"
                ).value(10))
                .andExpect(jsonPath(
                        "$.strengthProfile.pullUp.records[0].mode"
                ).value("BODYWEIGHT"));
    }

    // ==================== Duplicate Create ====================

    @Test
    void shouldRejectDuplicateProfile()
            throws Exception {

        Long userId = createUserId();

        String url = "/api/users/" + userId + "/profile";

        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isConflict());
    }

    // ==================== Missing Profile ====================

    @Test
    void shouldReturnNotFoundForMissingProfile()
            throws Exception {

        Long userId = createUserId();

        mockMvc.perform(
                        get("/api/users/" + userId + "/profile/current")
                )
                .andExpect(status().isNotFound());
    }

    // ==================== PATCH Integration ====================

    @Test
    void shouldPatchWeightAndPreserveOtherData()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        // 1. Create a complete body profile.
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isCreated());

        entityManager.flush();
        entityManager.clear();

        // 2. Update weight only.
        mockMvc.perform(
                        patch(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "weightKg": 72.0
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basicBodyInfo.weightKg")
                        .value(72.0));

        entityManager.flush();
        entityManager.clear();

        // 3. Read the profile again.
        mockMvc.perform(
                        get(url + "/current")
                )
                .andExpect(status().isOk())

                // Updated weight.
                .andExpect(jsonPath("$.basicBodyInfo.weightKg")
                        .value(72.0))

                // Unchanged basic information.
                .andExpect(jsonPath("$.basicBodyInfo.heightCm")
                        .value(175.0))
                .andExpect(jsonPath("$.basicBodyInfo.age")
                        .value(21))
                .andExpect(jsonPath("$.basicBodyInfo.bodyFatPercentage")
                        .value(15.0))

                // Unchanged body measurements.
                .andExpect(jsonPath(
                        "$.bodyMeasurements.waistCircumferenceCm"
                ).value(78.0))
                .andExpect(jsonPath(
                        "$.bodyMeasurements.leftArmCircumferenceCm"
                ).value(35.0))

                // Unchanged strength records.
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(70.0))
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[1].reps"
                ).value(10))
                .andExpect(jsonPath(
                        "$.strengthProfile.pullUp.records[0].reps"
                ).value(12));
    }


    // ==================== Invalid PATCH ====================

    @Test
    void shouldRejectInvalidPatchAndPreserveData()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        // 1. Create a valid profile.
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isCreated());

        // 2. Try to set an invalid weight.
        mockMvc.perform(
                        patch(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "weightKg": 500.0
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        entityManager.flush();
        entityManager.clear();

        // 3. Confirm the original weight remains.
        mockMvc.perform(
                        get(url + "/current")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basicBodyInfo.weightKg")
                        .value(70.0));
    }

    // ==================== History Integration ====================

    @Test
    void shouldCreatePatchAndReadHistory()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        // 1. Create the initial profile.
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isCreated());

        // 2. Update body weight.
        mockMvc.perform(
                        patch(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "weightKg": 72.0
                                }
                                """)
                )
                .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        // 3. Verify historical snapshots.
        mockMvc.perform(
                        get(url + "/history")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].weightKg")
                        .value(70.0))
                .andExpect(jsonPath("$[1].weightKg")
                        .value(72.0))
                .andExpect(jsonPath("$[0].waistCircumferenceCm")
                        .value(78.0))
                .andExpect(jsonPath("$[1].waistCircumferenceCm")
                        .value(78.0));
    }

    // ==================== Strength PATCH Integration ====================

    @Test
    void shouldPatchBenchPressAndPreserveOtherData()
            throws Exception {

        Long userId = createUserId();

        String url = "/api/users/" + userId + "/profile";

        // 1. Create a complete profile.
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson())
        ).andExpect(status().isCreated());

        entityManager.flush();
        entityManager.clear();

        // 2. Update bench press only.
        mockMvc.perform(
                        patch(url + "/strength")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "benchPress": [
                            {
                              "weightKg": 72.5,
                              "reps": 5,
                              "rir": 1
                            },
                            {
                              "weightKg": 65.0,
                              "reps": 10,
                              "rir": 2
                            }
                          ]
                        }
                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(72.5))
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[1].weightKg"
                ).value(65.0));

        entityManager.flush();
        entityManager.clear();

        // 3. Read the current profile again.
        mockMvc.perform(
                        get(url + "/current")
                )
                .andExpect(status().isOk())

                // Updated bench press.
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(72.5))

                // Original pull-up remains unchanged.
                .andExpect(jsonPath(
                        "$.strengthProfile.pullUp.records[0].reps"
                ).value(12))

                // Body measurements remain unchanged.
                .andExpect(jsonPath(
                        "$.basicBodyInfo.weightKg"
                ).value(70.0))

                .andExpect(jsonPath(
                        "$.bodyMeasurements.waistCircumferenceCm"
                ).value(78.0));
    }

    // ==================== Strength History Integration ====================

    @Test
    void shouldSaveStrengthHistoryAfterPatch()
            throws Exception {

        Long userId = createUserId();

        String url = "/api/users/" + userId + "/profile";

        // Create initial profile.
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson())
        ).andExpect(status().isCreated());

        // Update bench press.
        mockMvc.perform(
                patch(url + "/strength")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "benchPress": [
                            {
                              "weightKg": 72.5,
                              "reps": 5,
                              "rir": 1
                            }
                          ]
                        }
                        """)
        ).andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        Long profileId = profileRepository
                .findByUserId(userId)
                .orElseThrow()
                .getId();

        var snapshots = snapshotRepository
                .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                        profileId,
                        StrengthHistoryExerciseType.BENCH_PRESS
                );

        // Original and updated snapshots.
        assertEquals(2, snapshots.size());
    }

    // ==================== Pull-Up PATCH ====================

    @Test
    void shouldPatchPullUpAndPreserveBenchPress()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        // Create initial profile.
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson())
        ).andExpect(status().isCreated());

        // Change bodyweight pull-up to weighted pull-up.
        mockMvc.perform(
                        patch(url + "/strength")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "pullUp": [
                            {
                              "mode": "WEIGHTED",
                              "weightKg": 10.0,
                              "reps": 8,
                              "rir": 1
                            }
                          ]
                        }
                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.strengthProfile.pullUp.records[0].mode"
                ).value("WEIGHTED"))
                .andExpect(jsonPath(
                        "$.strengthProfile.pullUp.records[0].weightKg"
                ).value(10.0));

        entityManager.flush();
        entityManager.clear();

        // Verify data after reloading from database.
        mockMvc.perform(
                        get(url + "/current")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.strengthProfile.pullUp.records[0].reps"
                ).value(8))
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(70.0));

        Long profileId = profileRepository
                .findByUserId(userId)
                .orElseThrow()
                .getId();

        var snapshots = snapshotRepository
                .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                        profileId,
                        StrengthHistoryExerciseType.PULL_UP
                );

        // Original and updated pull-up snapshots.
        assertEquals(2, snapshots.size());
    }


    // ==================== Duplicate Strength PATCH ====================

    @Test
    void shouldNotCreateDuplicateStrengthHistory()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson())
        ).andExpect(status().isCreated());

        String patchJson = """
                {
                  "benchPress": [
                    {
                      "weightKg": 72.5,
                      "reps": 5,
                      "rir": 1
                    }
                  ]
                }
                """;

        // First update.
        mockMvc.perform(
                patch(url + "/strength")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        ).andExpect(status().isOk());

        // Repeat exactly the same update.
        mockMvc.perform(
                patch(url + "/strength")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        ).andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        Long profileId = profileRepository
                .findByUserId(userId)
                .orElseThrow()
                .getId();

        var snapshots = snapshotRepository
                .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                        profileId,
                        StrengthHistoryExerciseType.BENCH_PRESS
                );

        // One original snapshot and one updated snapshot.
        assertEquals(2, snapshots.size());
    }


    // ==================== Invalid Strength PATCH ====================

    @Test
    void shouldRejectInvalidStrengthPatchAndPreserveData()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson())
        ).andExpect(status().isCreated());

        // Weight exceeds the allowed 500 kg maximum.
        mockMvc.perform(
                patch(url + "/strength")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "benchPress": [
                            {
                              "weightKg": 600.0,
                              "reps": 5,
                              "rir": 1
                            }
                          ]
                        }
                        """)
        ).andExpect(status().isBadRequest());

        entityManager.flush();
        entityManager.clear();

        // The original bench press remains unchanged.
        mockMvc.perform(
                        get(url + "/current")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(70.0));

        Long profileId = profileRepository
                .findByUserId(userId)
                .orElseThrow()
                .getId();

        var snapshots = snapshotRepository
                .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                        profileId,
                        StrengthHistoryExerciseType.BENCH_PRESS
                );

        // An invalid update must not create history.
        assertEquals(0, snapshots.size());
    }


    // ==================== Strength Snapshot Content ====================

    @Test
    void shouldPreserveCompleteBenchPressHistory()
            throws Exception {

        Long userId = createUserId();
        String url = "/api/users/" + userId + "/profile";

        // 1. Create initial strength records.
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson())
        ).andExpect(status().isCreated());

        // 2. Update bench press records.
        mockMvc.perform(
                patch(url + "/strength")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "benchPress": [
                            {
                              "weightKg": 72.5,
                              "reps": 5,
                              "rir": 1
                            },
                            {
                              "weightKg": 65.0,
                              "reps": 10,
                              "rir": 2
                            }
                          ]
                        }
                        """)
        ).andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        Long profileId = profileRepository
                .findByUserId(userId)
                .orElseThrow()
                .getId();

        var snapshots = snapshotRepository
                .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                        profileId,
                        StrengthHistoryExerciseType.BENCH_PRESS
                );

        // Original snapshot and updated snapshot.
        assertEquals(2, snapshots.size());

        // Query order: newest first.
        var newSnapshot = snapshots.get(0);
        var oldSnapshot = snapshots.get(1);

        var oldRecords = historyRecordRepository
                .findBySnapshotIdOrderByRecordIndexAsc(
                        oldSnapshot.getId()
                );

        var newRecords = historyRecordRepository
                .findBySnapshotIdOrderByRecordIndexAsc(
                        newSnapshot.getId()
                );

        // ==================== Original Records ====================

        assertEquals(2, oldRecords.size());

        assertEquals(70.0, oldRecords.get(0).getWeightKg());
        assertEquals(5, oldRecords.get(0).getReps());
        assertEquals(1, oldRecords.get(0).getRir());

        assertEquals(60.0, oldRecords.get(1).getWeightKg());
        assertEquals(10, oldRecords.get(1).getReps());
        assertEquals(2, oldRecords.get(1).getRir());

        // ==================== Updated Records ====================

        assertEquals(2, newRecords.size());

        assertEquals(72.5, newRecords.get(0).getWeightKg());
        assertEquals(5, newRecords.get(0).getReps());
        assertEquals(1, newRecords.get(0).getRir());

        assertEquals(65.0, newRecords.get(1).getWeightKg());
        assertEquals(10, newRecords.get(1).getReps());
        assertEquals(2, newRecords.get(1).getRir());
    }

}
