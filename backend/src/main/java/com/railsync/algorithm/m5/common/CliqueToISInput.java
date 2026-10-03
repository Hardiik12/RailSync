package com.railsync.algorithm.m5.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CliqueToISInput {
    private List<String> vertices;
    private List<List<String>> edges;
    private Integer cliqueSize;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
