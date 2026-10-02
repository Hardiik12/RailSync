package com.railsync.algorithm.m2.sais.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SAISResponseDto {

    private final String algorithm;
    private final String text;
    private final int[] suffixArray;
    private final int lmsCount;
    private final int recursionDepth;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static SAISResponseDto fromResult(SAISResult result, String algorithmName) {
        return SAISResponseDto.builder()
                .algorithm(algorithmName)
                .text(result.getText())
                .suffixArray(result.getSuffixArray())
                .lmsCount(result.getLmsCount())
                .recursionDepth(result.getRecursionDepth())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
