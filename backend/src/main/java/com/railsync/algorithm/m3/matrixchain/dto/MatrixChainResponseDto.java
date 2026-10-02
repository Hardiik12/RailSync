package com.railsync.algorithm.m3.matrixchain.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MatrixChainResponseDto {

    private final String algorithm;
    private final int matrixCount;
    private final int[] dimensions;
    private final long minScalarMultiplications;
    private final long[][] dpTable;
    private final int[][] splitTable;
    private final String optimalParenthesization;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static MatrixChainResponseDto fromResult(MatrixChainResult result, String algorithmName) {
        return MatrixChainResponseDto.builder()
                .algorithm(algorithmName)
                .matrixCount(result.getMatrixCount())
                .dimensions(result.getDimensions())
                .minScalarMultiplications(result.getMinScalarMultiplications())
                .dpTable(result.getDpTable())
                .splitTable(result.getSplitTable())
                .optimalParenthesization(result.getOptimalParenthesization())
                .operationCount(result.getOperationCount())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
