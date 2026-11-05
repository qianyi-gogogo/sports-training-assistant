
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.request.goal.TrainingGoalRequest;
import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import static com.qianyi.sportstrainingassistant.model.TrainingGoalType.*;
import static org.junit.jupiter.api.Assertions.*;
import com.qianyi.sportstrainingassistant.dto.response.goal.TrainingGoalResponse;




@SpringBootTest
@Transactional
class TrainingGoalServiceTest {

    @Autowired
    private TrainingGoalService service;

    private Long testUserId() {
        return ThreadLocalRandom.current()
                .nextLong(1, Long.MAX_VALUE);
    }

    private TrainingGoalRequest createRequest() {
        TrainingGoalRequest request = new TrainingGoalRequest();

        request.setGoalPriorities(List.of(
                MUSCLE_GAIN,
                STRENGTH,
                FAT_LOSS,
                ENDURANCE,
                SPORT_PERFORMANCE
        ));

        request.setSpecificGoalDetails(
                "Improve chest development"
        );

        return request;
    }


    @Test
    void shouldCreateFirstGoalVersion() {
        Long userId = testUserId();

        TrainingGoalResponse result =
                service.createGoal(userId, createRequest());

        assertEquals(1, result.version());
        assertNotNull(result.createdAt());

        assertEquals(
                MUSCLE_GAIN,
                result.goalPriorities().get(0)
        );
    }


    @Test
    void shouldRejectDuplicateInitialGoal() {
        Long userId = testUserId();

        service.createGoal(userId, createRequest());

        assertThrows(
                IllegalStateException.class,
                () -> service.createGoal(
                        userId, createRequest()
                )
        );
    }

    @Test
    void shouldGetCurrentGoal() {
        Long userId = testUserId();

        service.createGoal(userId, createRequest());

        var goal = service.getCurrentGoal(userId);

        assertEquals(
                MUSCLE_GAIN,
                goal.getGoalPriorities().get(0)
        );
    }


    @Test
    void shouldCreateNewVersionWhenUpdating() {
        Long userId = testUserId();

        service.createGoal(userId, createRequest());

        TrainingGoalRequest updated = createRequest();

        updated.setGoalPriorities(List.of(
                STRENGTH,
                MUSCLE_GAIN,
                ENDURANCE,
                FAT_LOSS,
                SPORT_PERFORMANCE
        ));

        TrainingGoalResponse second =
                service.updateGoal(userId, updated);

        assertEquals(2, second.version());
        assertNotNull(second.createdAt());

        assertEquals(
                STRENGTH,
                service.getCurrentGoalResponse(userId)
                        .goalPriorities().get(0)
        );

        List<TrainingGoalResponse> history =
                service.getGoalHistoryResponses(userId);

        assertEquals(2, history.size());

        // Newest version first.
        assertEquals(2, history.get(0).version());
        assertEquals(1, history.get(1).version());

        // Original goal is preserved.
        assertEquals(
                MUSCLE_GAIN,
                history.get(1).goalPriorities().get(0)
        );
    }


    @Test
    void shouldRejectUpdateWithoutExistingGoal() {
        assertThrows(
                NoSuchElementException.class,
                () -> service.updateGoal(
                        testUserId(), createRequest()
                )
        );
    }

    @Test
    void shouldRejectInvalidUserId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createGoal(
                        0L, createRequest()
                )
        );
    }
}
