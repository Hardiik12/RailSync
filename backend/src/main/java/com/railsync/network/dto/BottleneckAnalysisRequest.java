package com.railsync.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BottleneckAnalysisRequest {
    private String sourceStationCode;
    private String sinkStationCode;
    private String algorithm;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
