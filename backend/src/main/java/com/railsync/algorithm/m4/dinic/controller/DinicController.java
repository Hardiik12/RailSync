package com.railsync.algorithm.m4.dinic.controller;

import com.railsync.algorithm.m4.dinic.dto.DinicInput;
import com.railsync.algorithm.m4.dinic.dto.DinicResponseDto;
import com.railsync.algorithm.m4.dinic.service.DinicService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m4/dinic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DinicController {

    private final DinicService dinicService;

    @PostMapping
    public ResponseEntity<ApiResponse<DinicResponseDto>> compute(@RequestBody DinicInput input) {
        DinicResponseDto response = dinicService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "maxFlow", response.getMaxFlow(),
                "phaseCount", response.getPhaseCount(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
