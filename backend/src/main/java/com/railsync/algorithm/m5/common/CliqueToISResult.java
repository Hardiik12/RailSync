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
public class CliqueToISResult {
    private String sourceProblem;
    private String targetProblem;
    private List<String> transformedVertices;
    private List<List<String>> transformedEdges;
    private int independentSetSize;
    private int cliqueSize;
    private boolean equivalent;
    private List<TraceStep> trace;
    private long executionTimeNanos;
    private long operationCount;
    private Complexity complexity;
}
