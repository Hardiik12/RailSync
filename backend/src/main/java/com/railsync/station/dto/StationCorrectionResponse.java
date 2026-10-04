package com.railsync.station.dto;

import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StationCorrectionResponse {
    private final String query;
    private final String algorithm;
    private final int totalStationsEvaluated;
    private final List<CandidateMatchDto> candidates;
    private final List<TraceStep> trace;
    private final Long executionTimeNanos;

    @Getter
    @Builder
    public static class CandidateMatchDto {
        private final StationDto station;
        private final int distance;
        private final String matchedField;
        private final String dataOrigin;
    }
}
