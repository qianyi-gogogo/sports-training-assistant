
package com.qianyi.sportstrainingassistant.dto.response.profile;

import com.qianyi.sportstrainingassistant.entity.BodyProfileHistoryEntity;

import java.time.LocalDateTime;

public record BodyProfileHistoryResponse(
        Long id,
        double weightKg,
        Double bodyFatPercentage,
        Double chestCircumferenceCm,
        Double waistCircumferenceCm,
        Double leftArmCircumferenceCm,
        Double rightArmCircumferenceCm,
        Double thighCircumferenceCm,
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
                entity.getBodyFatPercentage(),
                entity.getChestCircumferenceCm(),
                entity.getWaistCircumferenceCm(),
                entity.getLeftArmCircumferenceCm(),
                entity.getRightArmCircumferenceCm(),
                entity.getThighCircumferenceCm(),
                entity.getRecordedAt()
        );
    }
}
