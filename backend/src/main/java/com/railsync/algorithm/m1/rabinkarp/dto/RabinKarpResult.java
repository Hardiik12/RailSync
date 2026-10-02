package com.railsync.algorithm.m1.rabinkarp.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RabinKarpResult {
    private final List<Integer> matches;
    private final int matchCount;
    private final long patternHash;
    private final long primeModulus;
    private final int hashCollisions; // Hashes matched but character verification failed
    private final int hashVerifications; // Number of times character verification was triggered
    private final long comparisons; // Number of character comparisons performed
    private final long hashOperations; // Number of hash updates performed
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
