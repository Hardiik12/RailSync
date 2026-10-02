package com.railsync.algorithm.m1.ahocorasick.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AhoCorasickResponseDto {

    private final String algorithm;
    private final List<KeywordMatchDto> matches;
    private final int matchCount;
    private final List<String> matchedKeywords;
    private final List<TrieNodeDto> automatonNodes;
    private final long trieBuildNanos;
    private final long searchNanos;
    private final long executionTimeNanos;
    private final long operationCount;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static AhoCorasickResponseDto fromResult(AhoCorasickResult result, String algorithmName) {
        return AhoCorasickResponseDto.builder()
                .algorithm(algorithmName)
                .matches(result.getMatches())
                .matchCount(result.getMatchCount())
                .matchedKeywords(result.getMatchedKeywords())
                .automatonNodes(result.getAutomatonNodes())
                .trieBuildNanos(result.getTrieBuildNanos())
                .searchNanos(result.getSearchNanos())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .operationCount(result.getOperationCount())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
