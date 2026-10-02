package com.railsync.algorithm.m3.optimalbst.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OptimalBSTResponseDto {

    private final String algorithm;
    private final int keyCount;
    private final String[] keys;
    private final double[] frequencies;
    private final double minCost;
    private final double[][] dpTable;
    private final int[][] rootTable;
    private final BSTNodeDto rootNode;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static OptimalBSTResponseDto fromResult(OptimalBSTResult result, String algorithmName) {
        return OptimalBSTResponseDto.builder()
                .algorithm(algorithmName)
                .keyCount(result.getKeyCount())
                .keys(result.getKeys())
                .frequencies(result.getFrequencies())
                .minCost(result.getMinCost())
                .dpTable(result.getDpTable())
                .rootTable(result.getRootTable())
                .rootNode(result.getRootNode())
                .operationCount(result.getOperationCount())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
