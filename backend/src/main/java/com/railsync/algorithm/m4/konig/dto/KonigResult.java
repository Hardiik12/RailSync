package com.railsync.algorithm.m4.konig.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.m4.bipartitematching.dto.MatchedPairDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KonigResult {
    private int matchingSize;
    private int vertexCoverSize;
    private boolean sizesEqual;
    private List<MatchedPairDto> maximumMatching;
    private List<VertexCoverDto> minimumVertexCover;
    private List<String> alternatingReachableVertices;
    private long comparisons;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;
}
