package com.railsync.algorithm.m2.kasai.dto;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class KasaiResponseDto {

    private final String algorithm;
    private final String text;
    private final int[] suffixArray;
    private final int[] lcpArray;
    private final int maxLcpValue;
    private final String longestRepeatedSubstring;
    private final long operationCount;
    private final long executionTimeNanos;
    private final Complexity complexity;
    private final List<TraceStep> trace;

    public static KasaiResponseDto fromResult(KasaiResult result, String algorithmName) {
        return KasaiResponseDto.builder()
                .algorithm(algorithmName)
                .text(result.getText())
                .suffixArray(result.getSuffixArray())
                .lcpArray(result.getLcpArray())
                .maxLcpValue(result.getMaxLcpValue())
                .longestRepeatedSubstring(result.getLongestRepeatedSubstring())
                .operationCount(result.getComparisons())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .complexity(result.getComplexity())
                .trace(result.getTrace())
                .build();
    }
}
