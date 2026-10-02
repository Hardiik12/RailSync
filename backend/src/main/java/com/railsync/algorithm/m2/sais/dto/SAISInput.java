package com.railsync.algorithm.m2.sais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SAISInput {
    private String text;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
