package com.railsync.algorithm.m1.zfunction.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ZFunctionResponseDto {

    private final String algorithm;
    private final String processedString;
    private final int[] zArray;
    private final List<Integer> matches;
    private final int matchCount;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static ZFunctionResponseDto fromResult(ZFunctionResult result, String algorithmName) {
        return ZFunctionResponseDto.builder()
                .algorithm(algorithmName)
                .processedString(result.getProcessedString())
                .zArray(result.getZArray())
                .matches(result.getMatches())
                .matchCount(result.getMatchCount())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
