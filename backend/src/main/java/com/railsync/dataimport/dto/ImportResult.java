package com.railsync.dataimport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {
    @Builder.Default
    private String source = "PUBLIC_RAILWAY_DATASET";
    
    @Builder.Default
    private String snapshotDate = "2026-10-05";

    private int recordsRead;
    private int recordsInserted;
    private int recordsUpdated;
    private int duplicates;
    private int invalidRecords;
    private long durationMillis;

    private int tripsRead;
    private int tripsCreated;
    private int tripsUpdated;
    private int stopTimesRead;
    private int stopTimesCreated;
    private int stopTimesUpdated;

    @Builder.Default
    private List<String> warnings = new ArrayList<>();

    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
