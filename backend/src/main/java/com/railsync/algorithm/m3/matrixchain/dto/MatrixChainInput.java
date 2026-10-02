package com.railsync.algorithm.m3.matrixchain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrixChainInput {
    private int[] dimensions; // Array of matrix dimensions [p0, p1, p2, ..., pn]
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
