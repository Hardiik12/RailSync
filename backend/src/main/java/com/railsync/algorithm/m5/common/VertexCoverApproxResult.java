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
public class VertexCoverApproxResult {
    private String algorithm;
    private List<String> cover;
    private int coverSize;
    private int edgeCount;
    private List<List<String>> selectedEdges;
    private Map<String, Object> bound;
    private boolean isVerifiedCover;
    private List<TraceStep> trace;
    private long executionTimeNanos;
    private long operationCount;
    private Complexity complexity;
}
