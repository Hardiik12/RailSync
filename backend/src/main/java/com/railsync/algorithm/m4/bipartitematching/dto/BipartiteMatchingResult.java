package com.railsync.algorithm.m4.bipartitematching.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BipartiteMatchingResult {
    private int matchingSize;
    private List<MatchedPairDto> matchedPairs;
    private List<String> unmatchedLeftVertices;
    private List<String> unmatchedRightVertices;
    private long comparisons;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;
}
