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
public class KonigResponseDto {
    private String algorithm;
    private int matchingSize;
    private int vertexCoverSize;
    private boolean sizesEqual;
    private List<MatchedPairDto> maximumMatching;
    private List<VertexCoverDto> minimumVertexCover;
    private List<String> alternatingReachableVertices;
    private long operationCount;
    private long executionTimeNanos;
    private Complexity complexity;
    private List<TraceStep> trace;

    public static KonigResponseDto fromResult(KonigResult res, String algoName) {
        return KonigResponseDto.builder()
                .algorithm(algoName)
                .matchingSize(res.getMatchingSize())
                .vertexCoverSize(res.getVertexCoverSize())
                .sizesEqual(res.isSizesEqual())
                .maximumMatching(res.getMaximumMatching())
                .minimumVertexCover(res.getMinimumVertexCover())
                .alternatingReachableVertices(res.getAlternatingReachableVertices())
                .operationCount(res.getComparisons())
                .executionTimeNanos(res.getExecutionTimeNanos())
                .complexity(res.getComplexity())
                .trace(res.getTrace())
                .build();
    }
}
