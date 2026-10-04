package com.railsync.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationIndexResponse {
    private int recordsConsidered;
    private int documentsCreated;
    private int documentsUpdated;
    private int skippedRecords;
    private long durationMillis;
    private String source;
}
