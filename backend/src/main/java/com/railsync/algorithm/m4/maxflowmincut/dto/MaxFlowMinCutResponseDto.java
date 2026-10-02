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
public class MaxFlowMinCutResponseDto {
    private String algorithm;
    private double maxFlow;
    private double minCutCapacity;
    private boolean valuesEqual;
    private List<String> sourceCutVertices;
    private List<String> sinkCutVertices;
    private List<CutEdgeDto> cutEdges;
    private long operationCount;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;

    public static MaxFlowMinCutResponseDto fromResult(MaxFlowMinCutResult res, String algoName) {
        return MaxFlowMinCutResponseDto.builder()
                .algorithm(algoName)
                .maxFlow(res.getMaxFlow())
                .minCutCapacity(res.getMinCutCapacity())
                .valuesEqual(res.isValuesEqual())
                .sourceCutVertices(res.getSourceCutVertices())
                .sinkCutVertices(res.getSinkCutVertices())
                .cutEdges(res.getCutEdges())
                .operationCount(res.getComparisons())
                .executionTimeNanos(res.getExecutionTimeNanos())
                .complexity(res.getComplexity())
                .trace(res.getTrace())
                .build();
    }
}
