package com.railsync.algorithm.m4.fordfulkerson.service;

import com.railsync.algorithm.m4.fordfulkerson.FordFulkersonAlgorithm;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResponseDto;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FordFulkersonService {

    private static final int MAX_VERTICES = 500;

    private final FordFulkersonAlgorithm fordFulkersonAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public FordFulkersonResponseDto execute(FordFulkersonInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Input payload cannot be null");
        }

        if (input.getVertexCount() == null || input.getVertexCount() <= 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'vertexCount' must be positive integer > 0");
        }

        int n = input.getVertexCount();
        if (n > MAX_VERTICES) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("Vertex count (%d) exceeds maximum limit of %d", n, MAX_VERTICES));
        }

        if (input.getSource() == null || input.getSink() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameters 'source' and 'sink' are required");
        }

        int s = input.getSource();
        int t = input.getSink();
        if (s < 0 || s >= n || t < 0 || t >= n) {
            throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Source (%d) and Sink (%d) must be within range [0, %d]", s, t, n - 1));
        }

        if (s == t) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Source and Sink must be distinct vertices");
        }

        if (input.getEdges() == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'edges' cannot be null");
        }

        for (int i = 0; i < input.getEdges().size(); i++) {
            var edge = input.getEdges().get(i);
            if (edge == null) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Edge at index %d is null", i));
            }
            if (edge.getU() == null || edge.getV() == null) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Edge at index %d has missing endpoints", i));
            }
            if (edge.getU() < 0 || edge.getU() >= n || edge.getV() < 0 || edge.getV() >= n) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Edge at index %d has endpoints (%d, %d) outside vertex range [0, %d]", i, edge.getU(), edge.getV(), n - 1));
            }
            if (edge.getCapacity() == null || edge.getCapacity() < 0) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Edge at index %d has invalid or negative capacity", i));
            }
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        FordFulkersonInput validatedInput = FordFulkersonInput.builder()
                .vertexCount(n)
                .source(s)
                .sink(t)
                .edges(input.getEdges())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        FordFulkersonResult result = fordFulkersonAlgorithm.execute(validatedInput);
        return FordFulkersonResponseDto.fromResult(result, fordFulkersonAlgorithm.getName());
    }
}
