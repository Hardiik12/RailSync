package com.railsync.algorithm.m4.dinic.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.m4.common.dto.AugmentingPathDto;
import com.railsync.algorithm.m4.common.dto.FlowEdgeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DinicResponseDto {
    private String algorithm;
    private double maxFlow;
    private int phaseCount;
    private List<AugmentingPathDto> blockingFlows;
    private List<FlowEdgeDto> finalEdges;
    private long operationCount;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;

    public static DinicResponseDto fromResult(DinicResult res, String algoName) {
        return DinicResponseDto.builder()
                .algorithm(algoName)
                .maxFlow(res.getMaxFlow())
                .phaseCount(res.getPhaseCount())
                .blockingFlows(res.getBlockingFlows())
                .finalEdges(res.getFinalEdges())
                .operationCount(res.getComparisons())
                .executionTimeNanos(res.getExecutionTimeNanos())
                .complexity(res.getComplexity())
                .trace(res.getTrace())
                .build();
    }
}
