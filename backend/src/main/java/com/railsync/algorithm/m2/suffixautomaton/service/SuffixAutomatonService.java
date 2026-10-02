package com.railsync.algorithm.m2.suffixautomaton.service;

import com.railsync.algorithm.m2.suffixautomaton.SuffixAutomatonAlgorithm;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonInput;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonResponseDto;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuffixAutomatonService {

    private final SuffixAutomatonAlgorithm suffixAutomatonAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public SuffixAutomatonResponseDto execute(SuffixAutomatonInput input) {
        if (input == null || input.getText() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'text' is required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        SuffixAutomatonInput validatedInput = SuffixAutomatonInput.builder()
                .text(input.getText())
                .query(input.getQuery())
                .traceEnabled(input.getTraceEnabled())
                .benchmarkEnabled(input.getBenchmarkEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        SuffixAutomatonResult result = suffixAutomatonAlgorithm.execute(validatedInput);
        return SuffixAutomatonResponseDto.fromResult(result, suffixAutomatonAlgorithm.getName());
    }
}
