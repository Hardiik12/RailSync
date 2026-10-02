package com.railsync.algorithm.m3.levenshtein.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevenshteinInput {
    private String source;
    private String target;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
