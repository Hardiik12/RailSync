package com.railsync.algorithm.m1.kmp.controller;

import com.railsync.algorithm.m1.kmp.dto.KMPInput;
import com.railsync.algorithm.m1.kmp.dto.KMPResponseDto;
import com.railsync.algorithm.m1.kmp.service.KMPService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m1/kmp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KMPController {

    private final KMPService kmpService;

    @PostMapping
    public ResponseEntity<ApiResponse<KMPResponseDto>> search(@RequestBody KMPInput input) {
        KMPResponseDto response = kmpService.executeSearch(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
