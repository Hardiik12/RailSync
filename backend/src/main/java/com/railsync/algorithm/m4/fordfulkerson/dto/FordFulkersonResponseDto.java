package com.railsync.algorithm.m4.fordfulkerson.dto;

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
public class FordFulkersonResponseDto {
    private String algorithm;
    private double maxFlow;
    private List<AugmentingPathDto> augmentingPaths;
    private List<FlowEdgeDto> finalEdges;
    private long operationCount;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;

    public static FordFulkersonResponseDto fromResult(FordFulkersonResult res, String algoName) {
        return FordFulkersonResponseDto.builder()
                .algorithm(algoName)
                .maxFlow(res.getMaxFlow())
                .augmentingPaths(res.getAugmentingPaths())
                .finalEdges(res.getFinalEdges())
                .operationCount(res.getComparisons())
                .executionTimeNanos(res.getExecutionTimeNanos())
                .complexity(res.getComplexity())
                .trace(res.getTrace())
                .build();
    }
}
