package com.railsync.algorithm.m1.rabinkarp.controller;

import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpInput;
import com.railsync.algorithm.m1.rabinkarp.dto.RabinKarpResponseDto;
import com.railsync.algorithm.m1.rabinkarp.service.RabinKarpService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m1/rabin-karp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RabinKarpController {

    private final RabinKarpService rabinKarpService;

    @PostMapping
    public ResponseEntity<ApiResponse<RabinKarpResponseDto>> search(@RequestBody RabinKarpInput input) {
        RabinKarpResponseDto response = rabinKarpService.executeSearch(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
