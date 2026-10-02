package com.railsync.algorithm.m1.zfunction.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ZFunctionResult {
    private final String processedString;
    private final int[] zArray;
    private final List<Integer> matches;
    private final int matchCount;
    private final long comparisons;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
