package com.railsync.algorithm.m1.ahocorasick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AhoCorasickInput {
    private String text;
    private List<String> keywords;
    private Boolean traceEnabled;
    private Boolean benchmarkEnabled;
    private Integer maxTraceSteps;
}
