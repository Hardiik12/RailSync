package com.railsync.algorithm.m1.ahocorasick.service;

import com.railsync.algorithm.m1.ahocorasick.AhoCorasickAlgorithm;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickInput;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickResponseDto;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AhoCorasickService {

    public static final List<String> DEFAULT_RAILWAY_ALERT_KEYWORDS = List.of(
            "delay",
            "cancelled",
            "platform",
            "diverted",
            "maintenance",
            "rescheduled"
    );

    private final AhoCorasickAlgorithm ahoCorasickAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public AhoCorasickResponseDto executeSearch(AhoCorasickInput input) {
        if (input == null || input.getText() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'text' is required");
        }

        List<String> keywords = (input.getKeywords() != null && !input.getKeywords().isEmpty())
                ? input.getKeywords()
                : DEFAULT_RAILWAY_ALERT_KEYWORDS;

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        AhoCorasickInput validatedInput = AhoCorasickInput.builder()
                .text(input.getText())
                .keywords(keywords)
                .traceEnabled(input.getTraceEnabled())
                .benchmarkEnabled(input.getBenchmarkEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(validatedInput);
        return AhoCorasickResponseDto.fromResult(result, ahoCorasickAlgorithm.getName());
    }
}
