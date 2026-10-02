package com.railsync.algorithm.m1.zfunction.service;

import com.railsync.algorithm.m1.zfunction.ZFunctionAlgorithm;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionInput;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResponseDto;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZFunctionService {

    private final ZFunctionAlgorithm zFunctionAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public ZFunctionResponseDto execute(ZFunctionInput input) {
        if (input == null || input.getText() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'text' is required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        ZFunctionInput validatedInput = ZFunctionInput.builder()
                .text(input.getText())
                .pattern(input.getPattern())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        ZFunctionResult result = zFunctionAlgorithm.execute(validatedInput);
        return ZFunctionResponseDto.fromResult(result, zFunctionAlgorithm.getName());
    }
}
