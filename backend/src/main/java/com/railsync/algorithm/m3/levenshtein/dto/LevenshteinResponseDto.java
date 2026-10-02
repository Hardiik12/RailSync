package com.railsync.algorithm.m3.levenshtein.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LevenshteinResponseDto {

    private final String algorithm;
    private final String source;
    private final String target;
    private final int distance;
    private final int[][] dpMatrix;
    private final List<EditOperationDto> editOperations;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static LevenshteinResponseDto fromResult(LevenshteinResult result, String algorithmName) {
        return LevenshteinResponseDto.builder()
                .algorithm(algorithmName)
                .source(result.getSource())
                .target(result.getTarget())
                .distance(result.getDistance())
                .dpMatrix(result.getDpMatrix())
                .editOperations(result.getEditOperations())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
