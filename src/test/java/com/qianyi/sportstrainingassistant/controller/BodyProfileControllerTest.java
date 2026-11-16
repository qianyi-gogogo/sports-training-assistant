
package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.exception.GlobalExceptionHandler;
import com.qianyi.sportstrainingassistant.model.*;
import com.qianyi.sportstrainingassistant.service.BodyProfileService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BodyProfileController.class)
@Import(GlobalExceptionHandler.class)
class BodyProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BodyProfileService service;

    // ==================== Test Data ====================

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
        measurements.setLeftArmCircumferenceCm(35.0);
        measurements.setRightArmCircumferenceCm(34.5);

        BodyProfile profile = new BodyProfile(basic);
        profile.setBodyMeasurements(measurements);

        StrengthProfile strength = new StrengthProfile();

        strength.setBenchPress(new ExerciseStrength(
                List.of(
                        new StrengthRecord(70.0, 5, 1),
                        new StrengthRecord(60.0, 10, 2)
                )
        ));

        profile.setStrengthProfile(strength);

        return profile;
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
                    "rightArmCircumferenceCm": 34.5
                  },
                  "strengthProfile": {
                    "benchPress": [
                      {
                        "weightKg": 70.0,
                        "reps": 5,
                        "rir": 1
                      }
                    ]
                  }
                }
                """;
    }

    // ==================== Create ====================

    @Test
    void shouldCreateBodyProfile() throws Exception {

        when(service.createProfile(
                eq(1L),
                any(BodyProfile.class)
        )).thenReturn(createProfile());

        mockMvc.perform(
                        post("/api/users/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.basicBodyInfo.heightCm")
                        .value(175.0))
                .andExpect(jsonPath("$.basicBodyInfo.weightKg")
                        .value(70.0));

        verify(service).createProfile(
                eq(1L),
                any(BodyProfile.class)
        );
    }

    // ==================== Read ====================

    @Test
    void shouldGetCurrentProfile() throws Exception {

        when(service.getCurrentProfile(1L))
                .thenReturn(Optional.of(createProfile()));

        mockMvc.perform(
                        get("/api/users/1/profile/current")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basicBodyInfo.age")
                        .value(21))
                .andExpect(jsonPath(
                        "$.bodyMeasurements.waistCircumferenceCm"
                ).value(78.0))
                .andExpect(jsonPath(
                        "$.strengthProfile.benchPress.records[0].weightKg"
                ).value(70.0));
    }

    // ==================== Duplicate ====================

    @Test
    void shouldRejectDuplicateProfile() throws Exception {

        when(service.createProfile(
                eq(1L), any(BodyProfile.class)
        )).thenThrow(new IllegalStateException(
                "Body profile already exists for this user"
        ));

        mockMvc.perform(
                        post("/api/users/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJson())
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error")
                        .value("Body profile already exists for this user"));
    }

    // ==================== Not Found ====================

    @Test
    void shouldReturnNotFound() throws Exception {

        when(service.getCurrentProfile(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/api/users/99/profile/current")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Body profile not found"));
    }

    // ==================== Missing Basic Info ====================

    @Test
    void shouldRejectMissingBasicInfo() throws Exception {

        String invalidJson = """
                {
                  "bodyMeasurements": {
                    "waistCircumferenceCm": 78.0
                  }
                }
                """;

        mockMvc.perform(
                        post("/api/users/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest());

        verify(service, never())
                .createProfile(anyLong(), any());
    }

    // ==================== Invalid Strength ====================

    @Test
    void shouldRejectInvalidStrengthWeight() throws Exception {

        String invalidJson = """
                {
                  "basicBodyInfo": {
                    "heightCm": 175.0,
                    "weightKg": 70.0,
                    "age": 21,
                    "biologicalSex": "MALE"
                  },
                  "strengthProfile": {
                    "benchPress": [
                      {
                        "weightKg": 600.0,
                        "reps": 5
                      }
                    ]
                  }
                }
                """;

        mockMvc.perform(
                        post("/api/users/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest());

        verify(service, never())
                .createProfile(anyLong(), any());
    }

    // ==================== Invalid User ID ====================

    @Test
    void shouldRejectInvalidUserId() throws Exception {

        when(service.getCurrentProfile(-1L))
                .thenThrow(new IllegalArgumentException(
                        "User ID must be positive"
                ));

        mockMvc.perform(
                        get("/api/users/-1/profile/current")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("User ID must be positive"));
    }

    // ==================== Optional Data ====================

    @Test
    void shouldAcceptBasicInfoOnly() throws Exception {

        String minimalJson = """
                {
                  "basicBodyInfo": {
                    "heightCm": 175.0,
                    "weightKg": 70.0,
                    "age": 21,
                    "biologicalSex": "MALE"
                  }
                }
                """;

        BodyProfile minimalProfile = new BodyProfile(
                new BasicBodyInfo(
                        175.0, 70.0, 21, BiologicalSex.MALE
                )
        );

        when(service.createProfile(
                eq(2L), any(BodyProfile.class)
        )).thenReturn(minimalProfile);

        mockMvc.perform(
                        post("/api/users/2/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(minimalJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.basicBodyInfo.heightCm")
                        .value(175.0));

        verify(service).createProfile(
                eq(2L),
                argThat(profile ->
                        profile.getBodyMeasurements() == null
                                && profile.getStrengthProfile() == null
                )
        );
    }
}
