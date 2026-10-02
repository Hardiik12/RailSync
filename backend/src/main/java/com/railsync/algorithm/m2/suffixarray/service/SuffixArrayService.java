package com.railsync.algorithm.m2.suffixarray.service;

import com.railsync.algorithm.m2.suffixarray.SuffixArrayAlgorithm;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResponseDto;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuffixArrayService {

    private final SuffixArrayAlgorithm suffixArrayAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public SuffixArrayResponseDto execute(SuffixArrayInput input) {
        if (input == null || input.getText() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'text' is required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        SuffixArrayInput validatedInput = SuffixArrayInput.builder()
                .text(input.getText())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        SuffixArrayResult result = suffixArrayAlgorithm.execute(validatedInput);
        return SuffixArrayResponseDto.fromResult(result, suffixArrayAlgorithm.getName());
    }
}
