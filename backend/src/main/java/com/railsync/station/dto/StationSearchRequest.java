package com.railsync.station.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationSearchRequest {
    private String query;
    private String algorithm; // KMP, Z_FUNCTION, RABIN_KARP, AHO_CORASICK
    private String dataOriginFilter; // ALL, PUBLIC_DATA, SYNTHETIC
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
