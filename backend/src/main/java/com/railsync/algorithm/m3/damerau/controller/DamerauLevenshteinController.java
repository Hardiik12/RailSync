package com.railsync.algorithm.m3.damerau.controller;

import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinInput;
import com.railsync.algorithm.m3.damerau.dto.DamerauLevenshteinResponseDto;
import com.railsync.algorithm.m3.damerau.service.DamerauLevenshteinService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m3/damerau")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DamerauLevenshteinController {

    private final DamerauLevenshteinService damerauLevenshteinService;

    @PostMapping
    public ResponseEntity<ApiResponse<DamerauLevenshteinResponseDto>> compute(@RequestBody DamerauLevenshteinInput input) {
        DamerauLevenshteinResponseDto response = damerauLevenshteinService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "distance", response.getDistance(),
                "transpositionCount", response.getTranspositionCount(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
