package com.railsync.algorithm.m2.kasai.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class KasaiResult {
    private final String text;
    private final int[] suffixArray;
    private final int[] lcpArray;
    private final int maxLcpValue;
    private final String longestRepeatedSubstring;
    private final long comparisons;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
