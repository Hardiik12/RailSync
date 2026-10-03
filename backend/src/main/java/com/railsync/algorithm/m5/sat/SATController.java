package com.railsync.algorithm.m5.sat;

import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m5/sat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SATController {

    private final SATService satService;

    @PostMapping
    public ResponseEntity<ApiResponse<SATResult>> compute(@RequestBody SATInput input) {
        SATResult result = satService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", result.getAlgorithm(),
                "executionTimeNanos", result.getExecutionTimeNanos(),
                "satisfiable", result.isSatisfiable(),
                "operationCount", result.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }
}
