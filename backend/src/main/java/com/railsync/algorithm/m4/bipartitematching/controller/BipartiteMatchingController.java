package com.railsync.algorithm.m4.bipartitematching.controller;

import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingInput;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingResponseDto;
import com.railsync.algorithm.m4.bipartitematching.service.BipartiteMatchingService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m4/bipartite-matching")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BipartiteMatchingController {

    private final BipartiteMatchingService bipartiteMatchingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BipartiteMatchingResponseDto>> compute(@RequestBody BipartiteMatchingInput input) {
        BipartiteMatchingResponseDto response = bipartiteMatchingService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "matchingSize", response.getMatchingSize(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
