package com.railsync.algorithm.m2.sais.service;

import com.railsync.algorithm.m2.sais.SAISAlgorithm;
import com.railsync.algorithm.m2.sais.dto.SAISInput;
import com.railsync.algorithm.m2.sais.dto.SAISResponseDto;
import com.railsync.algorithm.m2.sais.dto.SAISResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SAISService {

    private final SAISAlgorithm saisAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public SAISResponseDto execute(SAISInput input) {
        if (input == null || input.getText() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'text' is required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        SAISInput validatedInput = SAISInput.builder()
                .text(input.getText())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        SAISResult result = saisAlgorithm.execute(validatedInput);
        return SAISResponseDto.fromResult(result, saisAlgorithm.getName());
    }
}
