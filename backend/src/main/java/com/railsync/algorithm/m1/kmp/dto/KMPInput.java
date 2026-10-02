package com.railsync.algorithm.m1.kmp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KMPInput {
    private String text;
    private String pattern;
    @Builder.Default
    private Boolean traceEnabled = false;
    @Builder.Default
    private Integer maxTraceSteps = 500;
}
