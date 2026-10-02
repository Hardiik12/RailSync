package com.railsync.algorithm.m1.kmp.service;

import com.railsync.algorithm.m1.kmp.KMPAlgorithm;
import com.railsync.algorithm.m1.kmp.dto.KMPInput;
import com.railsync.algorithm.m1.kmp.dto.KMPResponseDto;
import com.railsync.algorithm.m1.kmp.dto.KMPResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KMPService {

    private final KMPAlgorithm kmpAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public KMPResponseDto executeSearch(KMPInput input) {
        if (input == null || input.getText() == null || input.getPattern() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both 'text' and 'pattern' parameters are required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        KMPInput validatedInput = KMPInput.builder()
                .text(input.getText())
                .pattern(input.getPattern())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        KMPResult result = kmpAlgorithm.execute(validatedInput);
        return KMPResponseDto.fromResult(result, kmpAlgorithm.getName());
    }
}
