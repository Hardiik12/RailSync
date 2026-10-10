package com.railsync.network.dto;

import com.railsync.algorithm.common.TraceStep;
import com.railsync.station.dto.StationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BottleneckAnalysisResponse {
    private StationDto sourceStation;
    private StationDto destinationStation;
    private Double maxFlow;
    private Double minCutCapacity;
    private Double fordFulkersonFlow;
    private Double edmondsKarpFlow;
    private Double dinicFlow;
    private boolean crossValidationPassed;
    private String algorithm;
    private List<BottleneckSegmentDto> bottleneckSegments;
    private List<String> sourceCutVertices;
    private List<String> sinkCutVertices;
    private String explanation;
    private long executionTimeNanos;
    private List<TraceStep> trace;
}
