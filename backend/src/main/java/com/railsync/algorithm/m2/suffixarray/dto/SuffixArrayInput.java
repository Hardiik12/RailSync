package com.railsync.algorithm.m2.suffixarray.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuffixArrayInput {
    private String text;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
