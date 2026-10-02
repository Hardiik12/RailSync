package com.railsync.algorithm.m3.bitmask.controller;

import com.railsync.algorithm.m3.bitmask.dto.BitmaskInput;
import com.railsync.algorithm.m3.bitmask.dto.BitmaskResponseDto;
import com.railsync.algorithm.m3.bitmask.service.BitmaskService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m3/bitmask")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BitmaskController {

    private final BitmaskService bitmaskService;

    @PostMapping
    public ResponseEntity<ApiResponse<BitmaskResponseDto>> compute(@RequestBody BitmaskInput input) {
        BitmaskResponseDto response = bitmaskService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "optimalCost", response.getOptimalCost(),
                "stateCount", response.getStateCount(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
