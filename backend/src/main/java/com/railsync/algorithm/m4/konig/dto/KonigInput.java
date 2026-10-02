package com.railsync.algorithm.m4.konig.dto;

import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KonigInput {
    private List<String> leftVertices;
    private List<String> rightVertices;
    private List<BipartiteEdgeDto> edges;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;
}
