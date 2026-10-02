package com.railsync.algorithm.m2.suffixarray.controller;

import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayInput;
import com.railsync.algorithm.m2.suffixarray.dto.SuffixArrayResponseDto;
import com.railsync.algorithm.m2.suffixarray.service.SuffixArrayService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m2/suffix-array")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuffixArrayController {

    private final SuffixArrayService suffixArrayService;

    @PostMapping
    public ResponseEntity<ApiResponse<SuffixArrayResponseDto>> compute(@RequestBody SuffixArrayInput input) {
        SuffixArrayResponseDto response = suffixArrayService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
