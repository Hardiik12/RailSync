package com.railsync.algorithm.m2.suffixarray.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SuffixArrayResponseDto {

    private final String algorithm;
    private final String text;
    private final int[] suffixArray;
    private final int[] rankArray;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static SuffixArrayResponseDto fromResult(SuffixArrayResult result, String algorithmName) {
        return SuffixArrayResponseDto.builder()
                .algorithm(algorithmName)
                .text(result.getText())
                .suffixArray(result.getSuffixArray())
                .rankArray(result.getRankArray())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
