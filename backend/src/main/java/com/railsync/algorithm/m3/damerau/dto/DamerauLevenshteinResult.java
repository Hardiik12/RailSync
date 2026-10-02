package com.railsync.algorithm.m3.damerau.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.m3.levenshtein.dto.EditOperationDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DamerauLevenshteinResult {
    private final String source;
    private final String target;
    private final int distance;
    private final int[][] dpMatrix;
    private final List<EditOperationDto> editOperations;
    private final int transpositionCount;
    private final long comparisons;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
