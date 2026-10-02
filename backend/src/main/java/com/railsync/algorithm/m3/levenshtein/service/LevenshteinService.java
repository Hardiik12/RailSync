package com.railsync.algorithm.m3.levenshtein.service;

import com.railsync.algorithm.m3.levenshtein.LevenshteinAlgorithm;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResponseDto;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LevenshteinService {

    private static final int MAX_STRING_LENGTH = 500;

    private final LevenshteinAlgorithm levenshteinAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public LevenshteinResponseDto execute(LevenshteinInput input) {
        if (input == null || input.getSource() == null || input.getTarget() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both 'source' and 'target' parameters are required");
        }

        if (input.getSource().length() > MAX_STRING_LENGTH || input.getTarget().length() > MAX_STRING_LENGTH) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("String length exceeds maximum threshold of %d characters", MAX_STRING_LENGTH));
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        LevenshteinInput validatedInput = LevenshteinInput.builder()
                .source(input.getSource())
                .target(input.getTarget())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        LevenshteinResult result = levenshteinAlgorithm.execute(validatedInput);
        return LevenshteinResponseDto.fromResult(result, levenshteinAlgorithm.getName());
    }
}
