package com.railsync.algorithm.m3.optimalbst.service;

import com.railsync.algorithm.m3.optimalbst.OptimalBSTAlgorithm;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTInput;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTResponseDto;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimalBSTService {

    private static final int MAX_KEYS_COUNT = 50;

    private final OptimalBSTAlgorithm optimalBSTAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public OptimalBSTResponseDto execute(OptimalBSTInput input) {
        if (input == null || input.getKeys() == null || input.getFrequencies() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both 'keys' and 'frequencies' parameters are required");
        }

        if (input.getKeys().length != input.getFrequencies().length) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Array lengths of 'keys' and 'frequencies' must match");
        }

        if (input.getKeys().length > MAX_KEYS_COUNT) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("Key count (%d) exceeds maximum threshold of %d keys", input.getKeys().length, MAX_KEYS_COUNT));
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        OptimalBSTInput validatedInput = OptimalBSTInput.builder()
                .keys(input.getKeys())
                .frequencies(input.getFrequencies())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        OptimalBSTResult result = optimalBSTAlgorithm.execute(validatedInput);
        return OptimalBSTResponseDto.fromResult(result, optimalBSTAlgorithm.getName());
    }
}
