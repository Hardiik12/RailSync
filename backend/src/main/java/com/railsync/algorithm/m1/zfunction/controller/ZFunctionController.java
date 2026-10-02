package com.railsync.algorithm.m1.zfunction.controller;

import com.railsync.algorithm.m1.zfunction.dto.ZFunctionInput;
import com.railsync.algorithm.m1.zfunction.dto.ZFunctionResponseDto;
import com.railsync.algorithm.m1.zfunction.service.ZFunctionService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m1/z")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ZFunctionController {

    private final ZFunctionService zFunctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<ZFunctionResponseDto>> calculate(@RequestBody ZFunctionInput input) {
        ZFunctionResponseDto response = zFunctionService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
