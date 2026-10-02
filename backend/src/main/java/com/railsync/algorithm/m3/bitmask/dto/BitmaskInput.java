package com.railsync.algorithm.m3.bitmask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BitmaskInput {
    private Integer nodeCount;
    private double[][] costMatrix; // Adjacency matrix of weights (positive values, -1 or INF if disconnected)
    private Integer startNode;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
