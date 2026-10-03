package com.railsync.algorithm.m5.common;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SATResult {
    private String algorithm;
    private boolean satisfiable;
    private Map<String, Boolean> assignment;
    private List<TraceStep> trace;
    private long executionTimeNanos;
    private long operationCount;
    private Complexity complexity;
}
