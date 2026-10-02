package com.railsync.algorithm.m3.bitmask.service;

import com.railsync.algorithm.m3.bitmask.BitmaskAlgorithm;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskInput;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskResponseDto;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BitmaskService {

    private static final int MAX_BITMASK_NODES = 16;

    private final BitmaskAlgorithm bitmaskAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public BitmaskResponseDto execute(BitmaskInput input) {
        if (input == null || input.getNodeCount() == null || input.getCostMatrix() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameters 'nodeCount' and 'costMatrix' are required");
        }

        if (input.getNodeCount() > MAX_BITMASK_NODES) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("Bitmask node count (%d) exceeds maximum exponential safety threshold of %d nodes", input.getNodeCount(), MAX_BITMASK_NODES));
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        BitmaskInput validatedInput = BitmaskInput.builder()
                .nodeCount(input.getNodeCount())
                .costMatrix(input.getCostMatrix())
                .startNode(input.getStartNode())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        BitmaskResult result = bitmaskAlgorithm.execute(validatedInput);
        return BitmaskResponseDto.fromResult(result, bitmaskAlgorithm.getName());
    }
}
