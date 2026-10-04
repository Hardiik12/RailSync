package com.railsync.station.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationCorrectionRequest {
    private String query;
    private String algorithm; // LEVENSHTEIN, DAMERAU_LEVENSHTEIN
    private String dataOriginFilter; // ALL, PUBLIC_DATA, SYNTHETIC
    private Integer maxCandidates; // Default 5, max 20
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
