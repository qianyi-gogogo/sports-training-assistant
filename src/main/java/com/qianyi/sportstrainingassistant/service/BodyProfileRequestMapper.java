
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.request.profile.*;
import com.qianyi.sportstrainingassistant.model.*;

import java.util.List;

public final class BodyProfileRequestMapper {

    private BodyProfileRequestMapper() {
        // Utility class.
    }

    // ==================== Body Profile ====================

    public static BodyProfile toModel(
            BodyProfileRequest request
    ) {
        if (request == null
                || request.getBasicBodyInfo() == null) {
            throw new IllegalArgumentException(
                    "Basic body info is required"
            );
        }

        BasicBodyInfoRequest basicRequest =
                request.getBasicBodyInfo();

        if (basicRequest.getHeightCm() == null
                || basicRequest.getWeightKg() == null
                || basicRequest.getAge() == null
                || basicRequest.getBiologicalSex() == null) {
            throw new IllegalArgumentException(
                    "Required basic body fields are missing"
            );
        }

        BasicBodyInfo basic = new BasicBodyInfo(
                basicRequest.getHeightCm(),
                basicRequest.getWeightKg(),
                basicRequest.getAge(),
                basicRequest.getBiologicalSex()
        );

        basic.setBodyFatPercentage(
                basicRequest.getBodyFatPercentage()
        );

        BodyProfile profile = new BodyProfile(basic);

        // Optional measurements.
        if (request.getBodyMeasurements() != null) {
            profile.setBodyMeasurements(
                    mapMeasurements(
                            request.getBodyMeasurements()
                    )
            );
        }

        // Optional strength.
        if (request.getStrengthProfile() != null) {
            profile.setStrengthProfile(
                    mapStrength(
                            request.getStrengthProfile()
                    )
            );
        }

        return profile;
    }

    // ==================== Measurements ====================

    private static BodyMeasurements mapMeasurements(
            BodyMeasurementsRequest request
    ) {
        BodyMeasurements result = new BodyMeasurements();

        result.setChestCircumferenceCm(
                request.getChestCircumferenceCm()
        );

        result.setWaistCircumferenceCm(
                request.getWaistCircumferenceCm()
        );

        result.setLeftArmCircumferenceCm(
                request.getLeftArmCircumferenceCm()
        );

        result.setRightArmCircumferenceCm(
                request.getRightArmCircumferenceCm()
        );

        result.setThighCircumferenceCm(
                request.getThighCircumferenceCm()
        );

        return result;
    }

    // ==================== Strength ====================

    public static StrengthProfile mapStrength(
            StrengthProfileRequest request
    ) {
        StrengthProfile result = new StrengthProfile();

        result.setBenchPress(
                mapExercise(request.getBenchPress())
        );

        result.setBarbellRow(
                mapExercise(request.getBarbellRow())
        );

        result.setStrictPress(
                mapExercise(request.getStrictPress())
        );

        result.setDumbbellShoulderPress(
                mapExercise(request.getDumbbellShoulderPress())
        );

        result.setSquat(
                mapExercise(request.getSquat())
        );

        result.setLegPress(
                mapExercise(request.getLegPress())
        );

        if (request.getPullUp() != null) {
            List<PullUpRecord> records =
                    request.getPullUp()
                            .stream()
                            .map(record -> {
                                if (record == null
                                        || record.getMode() == null
                                        || record.getWeightKg() == null
                                        || record.getReps() == null) {
                                    throw new IllegalArgumentException(
                                            "Invalid pull-up record"
                                    );
                                }

                                return new PullUpRecord(
                                        record.getMode(),
                                        record.getWeightKg(),
                                        record.getReps(),
                                        record.getRir()
                                );
                            })
                            .toList();

            result.setPullUp(
                    new PullUpStrength(records)
            );
        }

        return result;
    }

    // ==================== Exercise Records ====================

    private static ExerciseStrength mapExercise(
            List<StrengthRecordRequest> requests
    ) {
        if (requests == null) {
            return null;
        }

        List<StrengthRecord> records = requests
                .stream()
                .map(record -> {
                    if (record == null
                            || record.getWeightKg() == null
                            || record.getReps() == null) {
                        throw new IllegalArgumentException(
                                "Invalid strength record"
                        );
                    }

                    return new StrengthRecord(
                            record.getWeightKg(),
                            record.getReps(),
                            record.getRir()
                    );
                })
                .toList();

        return new ExerciseStrength(records);
    }
}

