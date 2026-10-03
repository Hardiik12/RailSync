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
public class CliqueReductionResult {
    private String sourceProblem;
    private String targetProblem;
    private int targetCliqueSize;
    private List<String> vertices;
    private List<List<String>> edges;
    private Map<String, String> mapping;
    private boolean satisfiable;
    private List<String> cliqueFound;
    private List<TraceStep> trace;
    private long executionTimeNanos;
    private long operationCount;
    private Complexity complexity;
}
