
package com.qianyi.sportstrainingassistant.dto.response.auth;

import com.qianyi.sportstrainingassistant.model.EmailVerificationStatus;

public record EmailVerificationResponse(

        EmailVerificationStatus status,

        boolean verified,

        Integer remainingSendAttempts,

        Integer remainingVerificationAttempts,

        Long resendAfterSeconds,

        Long codeExpiresAfterSeconds,

        Long windowResetAfterSeconds

) {}
