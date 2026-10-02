package com.railsync.algorithm.m4.fordfulkerson.controller;

import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonInput;
import com.railsync.algorithm.m4.fordfulkerson.dto.FordFulkersonResponseDto;
import com.railsync.algorithm.m4.fordfulkerson.service.FordFulkersonService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m4/ford-fulkerson")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FordFulkersonController {

    private final FordFulkersonService fordFulkersonService;

    @PostMapping
    public ResponseEntity<ApiResponse<FordFulkersonResponseDto>> compute(@RequestBody FordFulkersonInput input) {
        FordFulkersonResponseDto response = fordFulkersonService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "maxFlow", response.getMaxFlow(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
