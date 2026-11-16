
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.request.profile.*;
import com.qianyi.sportstrainingassistant.model.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BodyProfileRequestMapperTest {

    // ==================== Test Data ====================

    private BodyProfileRequest createValidRequest() {

        BasicBodyInfoRequest basic =
                new BasicBodyInfoRequest();

        basic.setHeightCm(175.0);
        basic.setWeightKg(70.0);
        basic.setAge(21);
        basic.setBiologicalSex(BiologicalSex.MALE);
        basic.setBodyFatPercentage(15.0);

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(basic);

        return request;
    }

    // ==================== Basic Body Info ====================

    @Test
    void shouldConvertBasicBodyInfo() {

        BodyProfile result =
                BodyProfileRequestMapper.toModel(
                        createValidRequest()
                );

        assertEquals(175.0,
                result.getBasicBodyInfo().getHeightCm());

        assertEquals(70.0,
                result.getBasicBodyInfo().getWeightKg());

        assertEquals(21,
                result.getBasicBodyInfo().getAge());

        assertEquals(BiologicalSex.MALE,
                result.getBasicBodyInfo().getBiologicalSex());

        assertEquals(15.0,
                result.getBasicBodyInfo().getBodyFatPercentage());
    }

    // ==================== Measurements ====================

    @Test
    void shouldConvertBodyMeasurements() {

        BodyProfileRequest request =
                createValidRequest();

        BodyMeasurementsRequest measurements =
                new BodyMeasurementsRequest();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setWaistCircumferenceCm(78.0);
        measurements.setLeftArmCircumferenceCm(35.0);
        measurements.setRightArmCircumferenceCm(34.5);
        measurements.setThighCircumferenceCm(55.0);

        request.setBodyMeasurements(measurements);

        BodyProfile result =
                BodyProfileRequestMapper.toModel(request);

        BodyMeasurements restored =
                result.getBodyMeasurements();

        assertNotNull(restored);

        assertEquals(100.0,
                restored.getChestCircumferenceCm());

        assertEquals(78.0,
                restored.getWaistCircumferenceCm());

        assertEquals(35.0,
                restored.getLeftArmCircumferenceCm());

        assertEquals(34.5,
                restored.getRightArmCircumferenceCm());

        assertEquals(55.0,
                restored.getThighCircumferenceCm());
    }

    // ==================== Strength ====================

    @Test
    void shouldConvertStrengthRecords() {

        BodyProfileRequest request =
                createValidRequest();

        StrengthRecordRequest record =
                new StrengthRecordRequest();

        record.setWeightKg(70.0);
        record.setReps(5);
        record.setRir(1);

        StrengthProfileRequest strength =
                new StrengthProfileRequest();

        strength.setBenchPress(List.of(record));

        request.setStrengthProfile(strength);

        BodyProfile result =
                BodyProfileRequestMapper.toModel(request);

        StrengthRecord restored =
                result.getStrengthProfile()
                        .getBenchPress()
                        .getRecords()
                        .get(0);

        assertEquals(70.0, restored.getWeightKg());
        assertEquals(5, restored.getReps());
        assertEquals(1, restored.getRir());
    }

    // ==================== Optional Data ====================

    @Test
    void shouldAllowMissingOptionalData() {

        BodyProfile result =
                BodyProfileRequestMapper.toModel(
                        createValidRequest()
                );

        assertNull(result.getBodyMeasurements());
        assertNull(result.getStrengthProfile());
    }

    // ==================== Invalid Data ====================

    @Test
    void shouldRejectMissingBasicBodyInfo() {

        BodyProfileRequest request =
                new BodyProfileRequest();

        assertThrows(
                IllegalArgumentException.class,
                () -> BodyProfileRequestMapper.toModel(request)
        );
    }

    @Test
    void shouldRejectInvalidStrengthWeight() {

        BodyProfileRequest request =
                createValidRequest();

        StrengthRecordRequest record =
                new StrengthRecordRequest();

        record.setWeightKg(600.0);
        record.setReps(5);
        record.setRir(1);

        StrengthProfileRequest strength =
                new StrengthProfileRequest();

        strength.setBenchPress(List.of(record));
        request.setStrengthProfile(strength);

        assertThrows(
                IllegalArgumentException.class,
                () -> BodyProfileRequestMapper.toModel(request)
        );
    }
}
