package com.railsync.algorithm.m3.matrixchain.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MatrixChainResult {
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
}
