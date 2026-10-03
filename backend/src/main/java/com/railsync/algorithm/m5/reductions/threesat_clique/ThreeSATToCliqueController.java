package com.railsync.algorithm.m5.reductions.threesat_clique;

import com.railsync.algorithm.m5.common.CliqueReductionResult;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m5/3sat-to-clique")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThreeSATToCliqueController {

    private final ThreeSATToCliqueService service;

    @PostMapping
    public ResponseEntity<ApiResponse<CliqueReductionResult>> compute(@RequestBody SATInput input) {
        CliqueReductionResult result = service.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", result.getSourceProblem() + "_TO_" + result.getTargetProblem(),
                "executionTimeNanos", result.getExecutionTimeNanos(),
                "targetCliqueSize", result.getTargetCliqueSize(),
                "operationCount", result.getOperationCount()
        );

        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }
}
