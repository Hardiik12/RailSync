package com.railsync.algorithm.m3.bitmask.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class BitmaskResponseDto {

    private final String algorithm;
    private final int nodeCount;
    private final double optimalCost;
    private final List<Integer> pathSequence;
    private final Map<String, Double> dpStates;
    private final int stateCount;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static BitmaskResponseDto fromResult(BitmaskResult result, String algorithmName) {
        return BitmaskResponseDto.builder()
                .algorithm(algorithmName)
                .nodeCount(result.getNodeCount())
                .optimalCost(result.getOptimalCost())
                .pathSequence(result.getPathSequence())
                .dpStates(result.getDpStates())
                .stateCount(result.getStateCount())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
