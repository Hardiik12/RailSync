package com.railsync.algorithm.m2.suffixautomaton.controller;

import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonInput;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonResponseDto;
import com.railsync.algorithm.m2.suffixautomaton.service.SuffixAutomatonService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m2/suffix-automaton")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuffixAutomatonController {

    private final SuffixAutomatonService suffixAutomatonService;

    @PostMapping
    public ResponseEntity<ApiResponse<SuffixAutomatonResponseDto>> compute(@RequestBody SuffixAutomatonInput input) {
        SuffixAutomatonResponseDto response = suffixAutomatonService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "buildNanos", response.getBuildNanos(),
                "queryNanos", response.getQueryNanos(),
                "stateCount", response.getStateCount(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
