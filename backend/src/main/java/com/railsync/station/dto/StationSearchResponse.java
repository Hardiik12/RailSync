package com.railsync.station.dto;

import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StationSearchResponse {
    private final String query;
    private final String algorithm;
    private final int totalStationsSearched;
    private final int matchedStationCount;
    private final List<StationMatchDto> matches;
    private final List<TraceStep> trace;
    private final Long executionTimeNanos;

    @Getter
    @Builder
    public static class StationMatchDto {
        private final StationDto station;
        private final List<Integer> matchIndices;
        private final String matchedField;
    }
}
