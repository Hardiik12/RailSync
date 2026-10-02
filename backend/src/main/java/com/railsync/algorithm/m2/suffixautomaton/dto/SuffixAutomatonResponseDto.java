package com.railsync.algorithm.m2.suffixautomaton.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SuffixAutomatonResponseDto {

    private final String algorithm;
    private final String text;
    private final String query;
    private final boolean substringFound;
    private final int firstOccurrenceIndex;
    private final int stateCount;
    private final List<SAMStateDto> automatonStates;
    private final long buildNanos;
    private final long queryNanos;
    private final long executionTimeNanos;
    private final long operationCount;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static SuffixAutomatonResponseDto fromResult(SuffixAutomatonResult result, String algorithmName) {
        return SuffixAutomatonResponseDto.builder()
                .algorithm(algorithmName)
                .text(result.getText())
                .query(result.getQuery())
                .substringFound(result.isSubstringFound())
                .firstOccurrenceIndex(result.getFirstOccurrenceIndex())
                .stateCount(result.getStateCount())
                .automatonStates(result.getAutomatonStates())
                .buildNanos(result.getBuildNanos())
                .queryNanos(result.getQueryNanos())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .operationCount(result.getOperationCount())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
