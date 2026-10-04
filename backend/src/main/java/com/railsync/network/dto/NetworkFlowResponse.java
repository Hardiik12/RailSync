package com.railsync.network.dto;

import com.railsync.algorithm.m4.common.dto.FlowEdgeDto;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.station.dto.StationDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NetworkFlowResponse {
    private final Double maxFlow;
    private final StationDto sourceStation;
    private final StationDto sinkStation;
    private final String algorithm;
    private final Double fordFulkersonFlow;
    private final Double edmondsKarpFlow;
    private final Double dinicFlow;
    private final Boolean crossValidationPassed;
    private final List<FlowEdgeDto> edgesUsed;
    private final List<TraceStep> trace;
    private final Long executionTimeNanos;
}
