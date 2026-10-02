package com.railsync.algorithm.m1.ahocorasick.controller;

import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickInput;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickResponseDto;
import com.railsync.algorithm.m1.ahocorasick.service.AhoCorasickService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m1/aho-corasick")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AhoCorasickController {

    private final AhoCorasickService ahoCorasickService;

    @PostMapping
    public ResponseEntity<ApiResponse<AhoCorasickResponseDto>> search(@RequestBody AhoCorasickInput input) {
        AhoCorasickResponseDto response = ahoCorasickService.executeSearch(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "trieBuildNanos", response.getTrieBuildNanos(),
                "searchNanos", response.getSearchNanos(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
