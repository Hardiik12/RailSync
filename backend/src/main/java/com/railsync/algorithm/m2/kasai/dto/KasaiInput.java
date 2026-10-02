package com.railsync.algorithm.m2.kasai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KasaiInput {
    private String text;
    private int[] suffixArray; // Optional; if null, algorithm computes SA first
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
