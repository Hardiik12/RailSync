package com.railsync.algorithm.m2.kasai.service;

import com.railsync.algorithm.m2.kasai.KasaiAlgorithm;
import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResponseDto;
import com.railsync.algorithm.m2.kasai.dto.KasaiResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KasaiService {

    private final KasaiAlgorithm kasaiAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public KasaiResponseDto execute(KasaiInput input) {
        if (input == null || input.getText() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'text' is required");
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        KasaiInput validatedInput = KasaiInput.builder()
                .text(input.getText())
                .suffixArray(input.getSuffixArray())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        KasaiResult result = kasaiAlgorithm.execute(validatedInput);
        return KasaiResponseDto.fromResult(result, kasaiAlgorithm.getName());
    }
}
