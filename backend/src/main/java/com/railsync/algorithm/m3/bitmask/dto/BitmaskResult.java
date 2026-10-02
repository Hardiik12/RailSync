package com.railsync.algorithm.m3.bitmask.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class BitmaskResult {
    private final int nodeCount;
    private final double optimalCost;
    private final List<Integer> pathSequence;
    private final Map<String, Double> dpStates;
    private final int stateCount;
    private final long comparisons;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
