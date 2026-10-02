package com.railsync.algorithm.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutionMetrics {
    private final String algorithm;
    private final long executionTimeNanos;
    private final long operationCount;
    private final String timeComplexity;
    private final String spaceComplexity;
}
