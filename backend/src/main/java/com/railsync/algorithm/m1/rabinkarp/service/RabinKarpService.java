package com.railsync.algorithm.m1.rabinkarp.service;

import com.railsync.algorithm.m1.rabinkarp.RabinKarpAlgorithm;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpInput;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResponseDto;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabinKarpService {

    private final RabinKarpAlgorithm rabinKarpAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public RabinKarpResponseDto executeSearch(RabinKarpInput input) {
        if (input == null || input.getText() == null || input.getPattern() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Both 'text' and 'pattern' parameters are required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        RabinKarpInput validatedInput = RabinKarpInput.builder()
                .text(input.getText())
                .pattern(input.getPattern())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .primeModulus(input.getPrimeModulus())
                .build();

        RabinKarpResult result = rabinKarpAlgorithm.execute(validatedInput);
        return RabinKarpResponseDto.fromResult(result, rabinKarpAlgorithm.getName());
    }
}
