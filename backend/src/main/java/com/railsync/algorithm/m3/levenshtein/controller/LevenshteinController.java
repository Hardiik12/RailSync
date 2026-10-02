package com.railsync.algorithm.m3.levenshtein.controller;

import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinInput;
import com.railsync.algorithm.m3.levenshtein.dto.LevenshteinResponseDto;
import com.railsync.algorithm.m3.levenshtein.service.LevenshteinService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m3/levenshtein")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LevenshteinController {

    private final LevenshteinService levenshteinService;

    @PostMapping
    public ResponseEntity<ApiResponse<LevenshteinResponseDto>> compute(@RequestBody LevenshteinInput input) {
        LevenshteinResponseDto response = levenshteinService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "distance", response.getDistance(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
