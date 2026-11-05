
package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.response.goal.TrainingGoalResponse;
import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;
import com.qianyi.sportstrainingassistant.model.TrainingGoal;
import com.qianyi.sportstrainingassistant.service.TrainingGoalService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.qianyi.sportstrainingassistant.model.TrainingGoalType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.qianyi.sportstrainingassistant.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(TrainingGoalController.class)
@Import(GlobalExceptionHandler.class)
class TrainingGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainingGoalService service;

    private TrainingGoal createGoal() {
        return new TrainingGoal(
                List.of(
                        MUSCLE_GAIN,
                        STRENGTH,
                        FAT_LOSS,
                        ENDURANCE,
                        SPORT_PERFORMANCE
                ),
                "Improve chest development"
        );
    }

    private TrainingGoalResponse createResponse(int version) {
        return new TrainingGoalResponse(
                version,
                LocalDateTime.of(2026, 11, 2, 12, 0),
                createGoal().getGoalPriorities(),
                createGoal().getSpecificGoalDetails()
        );
    }

    private String validJson() {
        return """
                {
                  "goalPriorities": [
                    "MUSCLE_GAIN",
                    "STRENGTH",
                    "FAT_LOSS",
                    "ENDURANCE",
                    "SPORT_PERFORMANCE"
                  ],
                  "specificGoalDetails": "Improve chest development"
                }
                """;
    }


    @Test
    void shouldCreateInitialGoal() throws Exception {
        when(service.createGoal(eq(1L), any()))
                .thenReturn(createResponse(1));

        mockMvc.perform(post("/api/users/1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.goalPriorities[0]")
                        .value("MUSCLE_GAIN"));

        verify(service).createGoal(eq(1L), any());
    }


    @Test
    void shouldGetCurrentGoalWithMetadata() throws Exception {
        when(service.getCurrentGoalResponse(1L))
                .thenReturn(createResponse(3));

        mockMvc.perform(get("/api/users/1/goals/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(3))
                .andExpect(jsonPath("$.createdAt")
                        .value("2026-11-02T12:00:00"));
    }


    @Test
    void shouldCreateNewGoalVersion() throws Exception {
        when(service.updateGoal(eq(1L), any()))
                .thenReturn(createResponse(2));

        mockMvc.perform(post("/api/users/1/goals/versions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.version").value(2));

        verify(service).updateGoal(eq(1L), any());
    }


    @Test
    void shouldReturnGoalHistory() throws Exception {
        when(service.getGoalHistoryResponses(1L))
                .thenReturn(List.of(
                        createResponse(3),
                        createResponse(2),
                        createResponse(1)
                ));

        mockMvc.perform(get("/api/users/1/goals/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].version").value(3))
                .andExpect(jsonPath("$[2].version").value(1));
    }

    @Test
    void shouldRejectDuplicateInitialGoal() throws Exception {
        when(service.createGoal(eq(1L), any()))
                .thenThrow(new IllegalStateException(
                        "User already has a training goal"
                ));

        mockMvc.perform(post("/api/users/1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundForMissingGoal() throws Exception {
        when(service.getCurrentGoalResponse(99L))
                .thenThrow(new NoSuchElementException(
                        "Training goal not found"
                ));

        mockMvc.perform(get("/api/users/99/goals/current"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidRequest() throws Exception {
        String invalidJson = """
                {
                  "goalPriorities": [
                    "MUSCLE_GAIN",
                    "STRENGTH"
                  ]
                }
                """;

        mockMvc.perform(post("/api/users/1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .createGoal(anyLong(), any());
    }

    @Test
    void shouldReturnErrorMessageForMissingGoal()
            throws Exception {

        when(service.getCurrentGoalResponse(99L))
                .thenThrow(new NoSuchElementException(
                        "Training goal not found"
                ));

        mockMvc.perform(
                        get("/api/users/99/goals/current"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Training goal not found"));
    }

    @Test
    void shouldReturnValidationErrorForInvalidRequest()
            throws Exception {

        String invalidJson = """
                {
                  "goalPriorities": [
                    "MUSCLE_GAIN"
                  ]
                }
                """;

        mockMvc.perform(
                        post("/api/users/1/goals")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(service, never())
                .createGoal(anyLong(), any());
    }

}
