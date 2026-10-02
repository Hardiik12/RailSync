package com.railsync.algorithm.m2.suffixautomaton.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SuffixAutomatonResult {
    private final String text;
    private final String query;
    private final boolean substringFound;
    private final int firstOccurrenceIndex;
    private final int stateCount;
    private final List<SAMStateDto> automatonStates;
    private final long buildNanos;
    private final long queryNanos;
    private final long executionTimeNanos;
    private final long operationCount;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
