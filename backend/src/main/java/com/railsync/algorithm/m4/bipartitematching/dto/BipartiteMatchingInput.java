package com.railsync.algorithm.m4.bipartitematching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BipartiteMatchingInput {
    private List<String> leftVertices;
    private List<String> rightVertices;
    private List<BipartiteEdgeDto> edges;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
