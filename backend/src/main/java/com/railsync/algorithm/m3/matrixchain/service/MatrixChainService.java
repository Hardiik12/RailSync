package com.railsync.algorithm.m3.matrixchain.service;

import com.railsync.algorithm.m3.matrixchain.MatrixChainAlgorithm;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainInput;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainResponseDto;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatrixChainService {

    private static final int MAX_MATRIX_COUNT = 50;

    private final MatrixChainAlgorithm matrixChainAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public MatrixChainResponseDto execute(MatrixChainInput input) {
        if (input == null || input.getDimensions() == null || input.getDimensions().length < 2) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'dimensions' must contain at least 2 values");
        }

        int n = input.getDimensions().length - 1;
        if (n > MAX_MATRIX_COUNT) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("Matrix count (%d) exceeds maximum threshold of %d matrices", n, MAX_MATRIX_COUNT));
        }

        for (int dim : input.getDimensions()) {
            if (dim <= 0) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Matrix dimensions must be positive integers");
            }
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        MatrixChainInput validatedInput = MatrixChainInput.builder()
                .dimensions(input.getDimensions())
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        MatrixChainResult result = matrixChainAlgorithm.execute(validatedInput);
        return MatrixChainResponseDto.fromResult(result, matrixChainAlgorithm.getName());
    }
}
