package com.railsync.algorithm.m1.rabinkarp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabinKarpInput {
    private String text;
    private String pattern;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
    private Integer primeModulus; // Optional override for prime modulus, default 1000000007 (or 101 for testing collisions)
}
