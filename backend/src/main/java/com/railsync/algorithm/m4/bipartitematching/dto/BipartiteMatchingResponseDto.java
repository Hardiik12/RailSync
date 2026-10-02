package com.railsync.algorithm.m4.bipartitematching.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BipartiteMatchingResponseDto {
    private String algorithm;
    private int matchingSize;
    private List<MatchedPairDto> matchedPairs;
    private List<String> unmatchedLeftVertices;
    private List<String> unmatchedRightVertices;
    private long operationCount;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;

    public static BipartiteMatchingResponseDto fromResult(BipartiteMatchingResult res, String algoName) {
        return BipartiteMatchingResponseDto.builder()
                .algorithm(algoName)
                .matchingSize(res.getMatchingSize())
                .matchedPairs(res.getMatchedPairs())
                .unmatchedLeftVertices(res.getUnmatchedLeftVertices())
                .unmatchedRightVertices(res.getUnmatchedRightVertices())
                .operationCount(res.getComparisons())
                .executionTimeNanos(res.getExecutionTimeNanos())
                .complexity(res.getComplexity())
                .trace(res.getTrace())
                .build();
    }
}
