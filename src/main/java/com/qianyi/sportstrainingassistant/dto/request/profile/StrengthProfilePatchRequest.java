
package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.constraints.AssertTrue;

public class StrengthProfilePatchRequest
        extends StrengthProfileRequest {

    // ==================== PATCH Validation ====================

    @AssertTrue(message = "At least one strength exercise must be provided")
    public boolean isUpdateProvided() {

        return getBenchPress() != null
                || getBarbellRow() != null
                || getPullUp() != null
                || getStrictPress() != null
                || getDumbbellShoulderPress() != null
                || getSquat() != null
                || getLegPress() != null;
    }
}
