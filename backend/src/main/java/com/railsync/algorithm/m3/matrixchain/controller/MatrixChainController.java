package com.railsync.algorithm.m3.matrixchain.controller;

import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainInput;
import com.railsync.algorithm.m3.matrixchain.dto.MatrixChainResponseDto;
import com.railsync.algorithm.m3.matrixchain.service.MatrixChainService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m3/matrix-chain")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MatrixChainController {

    private final MatrixChainService matrixChainService;

    @PostMapping
    public ResponseEntity<ApiResponse<MatrixChainResponseDto>> compute(@RequestBody MatrixChainInput input) {
        MatrixChainResponseDto response = matrixChainService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "minScalarMultiplications", response.getMinScalarMultiplications(),
                "optimalParenthesization", response.getOptimalParenthesization(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
