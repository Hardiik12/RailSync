package com.railsync.algorithm.m3.optimalbst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimalBSTInput {
    private String[] keys; // Ordered keys (e.g. station codes: ["NDLS", "PUNE", "SBC"])
    private double[] frequencies; // Corresponding access frequencies
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
