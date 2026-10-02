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
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Input payload cannot be null");
        }

        if (input.getNodeCount() == null || input.getNodeCount() <= 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'nodeCount' must be a positive integer > 0");
        }

        int n = input.getNodeCount();
        if (n > MAX_BITMASK_NODES) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("Bitmask node count (%d) exceeds maximum exponential safety threshold of %d nodes", n, MAX_BITMASK_NODES));
        }

        int startNode = input.getStartNode() != null ? input.getStartNode() : 0;
        if (startNode < 0 || startNode >= n) {
            throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Parameter 'startNode' (%d) must be within range [0, %d]", startNode, n - 1));
        }

        double[][] costMatrix = input.getCostMatrix();
        if (costMatrix == null || costMatrix.length < n) {
            throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Parameter 'costMatrix' must contain at least %d rows", n));
        }

        for (int i = 0; i < n; i++) {
            if (costMatrix[i] == null || costMatrix[i].length < n) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Row %d of 'costMatrix' must contain at least %d columns", i, n));
            }
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        BitmaskInput validatedInput = BitmaskInput.builder()
                .nodeCount(n)
                .costMatrix(costMatrix)
                .startNode(startNode)
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        BitmaskResult result = bitmaskAlgorithm.execute(validatedInput);
        return BitmaskResponseDto.fromResult(result, bitmaskAlgorithm.getName());
    }
}

