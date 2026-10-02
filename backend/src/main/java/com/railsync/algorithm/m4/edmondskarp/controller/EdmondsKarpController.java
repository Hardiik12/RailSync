package com.railsync.algorithm.m4.edmondskarp.controller;

import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpInput;
import com.railsync.algorithm.m4.edmondskarp.dto.EdmondsKarpResponseDto;
import com.railsync.algorithm.m4.edmondskarp.service.EdmondsKarpService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m4/edmonds-karp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EdmondsKarpController {

    private final EdmondsKarpService edmondsKarpService;

    @PostMapping
    public ResponseEntity<ApiResponse<EdmondsKarpResponseDto>> compute(@RequestBody EdmondsKarpInput input) {
        EdmondsKarpResponseDto response = edmondsKarpService.execute(input);

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
