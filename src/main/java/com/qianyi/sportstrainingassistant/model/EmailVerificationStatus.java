package com.qianyi.sportstrainingassistant.model;

public enum EmailVerificationStatus {

    CODE_SENT,

    VERIFIED,

    INVALID_CODE,

    CODE_EXPIRED,

    RESEND_COOLDOWN,

    SEND_LIMIT_REACHED,

    ATTEMPT_LIMIT_REACHED,

    INVALID_OR_UNAVAILABLE

}