package com.railsync.algorithm.m3.optimalbst.controller;

import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTInput;
import com.railsync.algorithm.m3.optimalbst.dto.OptimalBSTResponseDto;
import com.railsync.algorithm.m3.optimalbst.service.OptimalBSTService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m3/optimal-bst")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OptimalBSTController {

    private final OptimalBSTService optimalBSTService;

    @PostMapping
    public ResponseEntity<ApiResponse<OptimalBSTResponseDto>> compute(@RequestBody OptimalBSTInput input) {
        OptimalBSTResponseDto response = optimalBSTService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "minCost", response.getMinCost(),
                "keyCount", response.getKeyCount(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
