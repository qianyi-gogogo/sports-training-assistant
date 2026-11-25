USE sports_training_assistant;

ALTER TABLE email_verification_codes
    ADD COLUMN send_count INT NOT NULL DEFAULT 1,
    ADD COLUMN window_started_at DATETIME(6)
        NOT NULL DEFAULT CURRENT_TIMESTAMP(6);