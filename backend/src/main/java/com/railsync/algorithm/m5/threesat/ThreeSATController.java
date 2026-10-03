package com.railsync.algorithm.m5.threesat;

import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m5/3sat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThreeSATController {

    private final ThreeSATService threeSATService;

    @PostMapping
    public ResponseEntity<ApiResponse<SATResult>> compute(@RequestBody SATInput input) {
        SATResult result = threeSATService.execute(input);

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
