
package com.qianyi.sportstrainingassistant.dto.response.profile;

import com.qianyi.sportstrainingassistant.entity.BodyProfileHistoryEntity;

import java.time.LocalDateTime;

public record BodyProfileHistoryResponse(
        Long id,
        double weightKg,
        Double waistCircumferenceCm,
        LocalDateTime recordedAt
) {

    // ==================== Entity To Response ====================

    public static BodyProfileHistoryResponse fromEntity(
            BodyProfileHistoryEntity entity
    ) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Body profile history entity cannot be null"
            );
        }

        return new BodyProfileHistoryResponse(
                entity.getId(),
                entity.getWeightKg(),
                entity.getWaistCircumferenceCm(),
                entity.getRecordedAt()
        );
    }
}
