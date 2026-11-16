
package com.qianyi.sportstrainingassistant.controller;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

}
