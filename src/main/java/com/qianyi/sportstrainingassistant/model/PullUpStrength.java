package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class PullUpStrength {

    private final List<PullUpRecord> records;


    public PullUpStrength(List<PullUpRecord> records) {

        validateRecords(records);

        this.records = List.copyOf(records);
    }


    public List<PullUpRecord> getRecords() {
        return records;
    }


    // ==================== Validation ====================

    private void validateRecords(List<PullUpRecord> records) {

        if (records == null) {
            throw new IllegalArgumentException(
                    "Pull-up records cannot be null"
            );
        }

        if (records.isEmpty()) {
            throw new IllegalArgumentException(
                    "Pull-up records cannot be empty"
            );
        }

        for (PullUpRecord record : records) {
            if (record == null) {
                throw new IllegalArgumentException(
                        "Pull-up records cannot contain null"
                );
            }
        }
    }
}