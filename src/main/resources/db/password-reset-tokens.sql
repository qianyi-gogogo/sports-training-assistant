-- Run once in the sports_training_assistant database before starting the updated app.
-- This repository uses spring.jpa.hibernate.ddl-auto=validate, so Hibernate will not create the table.
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    requested_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_reset_user UNIQUE (user_id),
    CONSTRAINT uk_reset_hash UNIQUE (token_hash),
    CONSTRAINT fk_reset_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;
