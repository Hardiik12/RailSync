package com.railsync.algorithm.m4.maxflowmincut.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaxFlowMinCutResult {
    private double maxFlow;
    private double minCutCapacity;
    private boolean valuesEqual;
    private List<String> sourceCutVertices;
    private List<String> sinkCutVertices;
    private List<CutEdgeDto> cutEdges;
    private long comparisons;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;
}
