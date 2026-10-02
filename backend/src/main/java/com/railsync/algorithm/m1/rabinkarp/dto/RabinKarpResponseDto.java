package com.railsync.algorithm.m1.rabinkarp.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RabinKarpResponseDto {

    private final String algorithm;
    private final List<Integer> matches;
    private final int matchCount;
    private final long patternHash;
    private final long primeModulus;
    private final int hashCollisions;
    private final int hashVerifications;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static RabinKarpResponseDto fromResult(RabinKarpResult result, String algorithmName) {
        return RabinKarpResponseDto.builder()
                .algorithm(algorithmName)
                .matches(result.getMatches())
                .matchCount(result.getMatchCount())
                .patternHash(result.getPatternHash())
                .primeModulus(result.getPrimeModulus())
                .hashCollisions(result.getHashCollisions())
                .hashVerifications(result.getHashVerifications())
                .operationCount(result.getComparisons() + result.getHashOperations())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
