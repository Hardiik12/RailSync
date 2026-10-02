package com.railsync.algorithm.m1.kmp.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class KMPResponseDto {

    private final String algorithm;
    private final List<Integer> matches;
    private final int matchCount;
    private final int[] lps;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static KMPResponseDto fromResult(KMPResult result, String algorithmName) {
        return KMPResponseDto.builder()
                .algorithm(algorithmName)
                .matches(result.getMatches())
                .matchCount(result.getMatchCount())
                .lps(result.getLps())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
