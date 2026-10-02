package com.railsync.algorithm.m3.optimalbst.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OptimalBSTResult {
    private final int keyCount;
    private final String[] keys;
    private final double[] frequencies;
    private final double minCost;
    private final double[][] dpTable;
    private final int[][] rootTable;
    private final BSTNodeDto rootNode;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
