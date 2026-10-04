package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class ExerciseStrength {

    private final List<StrengthRecord> records;


    public ExerciseStrength(List<StrengthRecord> records) {

        if (records == null) {
            throw new IllegalArgumentException(
                    "Strength records cannot be null"
            );
        }

        if (records.isEmpty()) {
            throw new IllegalArgumentException(
                    "Strength records cannot be empty"
            );
        }

        for (StrengthRecord record : records) {
            if (record == null) {
                throw new IllegalArgumentException(
                        "Strength records cannot contain null"
                );
            }
        }

        this.records = List.copyOf(records);
    }


    public List<StrengthRecord> getRecords() {
        return records;
    }
}