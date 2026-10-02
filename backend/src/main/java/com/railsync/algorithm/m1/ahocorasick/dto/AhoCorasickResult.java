package com.railsync.algorithm.m1.ahocorasick.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AhoCorasickResult {
    private final List<KeywordMatchDto> matches;
    private final int matchCount;
    private final List<String> matchedKeywords;
    private final List<TrieNodeDto> automatonNodes;
    private final long trieBuildNanos;
    private final long searchNanos;
    private final long executionTimeNanos;
    private final long operationCount;
    private final Complexity complexity;
    private final List<TraceStep> trace;
}
