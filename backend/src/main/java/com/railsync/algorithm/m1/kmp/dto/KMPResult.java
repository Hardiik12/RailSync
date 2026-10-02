package com.railsync.algorithm.m1.kmp.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class KMPResult {
    private final List<Integer> matches;
    private final int matchCount;
    private final int[] lps;
    private final long comparisons;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
