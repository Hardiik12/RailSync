package com.railsync.algorithm.m4.maxflowmincut.controller;

import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutInput;
import com.railsync.algorithm.m4.maxflowmincut.dto.MaxFlowMinCutResponseDto;
import com.railsync.algorithm.m4.maxflowmincut.service.MaxFlowMinCutService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m4/max-flow-min-cut")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaxFlowMinCutController {

    private final MaxFlowMinCutService maxFlowMinCutService;

    @PostMapping
    public ResponseEntity<ApiResponse<MaxFlowMinCutResponseDto>> compute(@RequestBody MaxFlowMinCutInput input) {
        MaxFlowMinCutResponseDto response = maxFlowMinCutService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "maxFlow", response.getMaxFlow(),
                "minCutCapacity", response.getMinCutCapacity(),
                "valuesEqual", response.isValuesEqual(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
