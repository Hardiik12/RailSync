package com.railsync.algorithm.m5.common;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ISToVCResult {
    private String sourceProblem;
    private String targetProblem;
    private int vertexCount;
    private int independentSetSize;
    private int vertexCoverSize;
    private List<String> independentSet;
    private List<String> vertexCover;
    private boolean verified;
    private List<TraceStep> trace;
    private long executionTimeNanos;
    private long operationCount;
    private Complexity complexity;
}
