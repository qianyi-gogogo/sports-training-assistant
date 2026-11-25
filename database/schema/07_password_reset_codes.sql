-- Password reset codes use an independent rate-limit window.
USE sports_training_assistant;

CREATE TABLE IF NOT EXISTS password_reset_codes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    code_hash VARCHAR(100) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    failed_attempts INT NOT NULL DEFAULT 0,
    send_count INT NOT NULL DEFAULT 1,
    window_started_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_password_reset_user UNIQUE (user_id),
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;
