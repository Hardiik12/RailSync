package com.railsync.algorithm.m2.suffixautomaton.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuffixAutomatonInput {
    private String text;
    private String query; // Optional query substring to test existence
    private Boolean traceEnabled;
    private Boolean benchmarkEnabled;
    private Integer maxTraceSteps;
}
