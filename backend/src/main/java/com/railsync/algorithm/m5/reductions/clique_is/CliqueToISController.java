package com.railsync.algorithm.m5.reductions.clique_is;

import com.railsync.algorithm.m5.common.CliqueToISInput;
import com.railsync.algorithm.m5.common.CliqueToISResult;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m5/clique-to-independent-set")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CliqueToISController {

    private final CliqueToISService service;

    @PostMapping
    public ResponseEntity<ApiResponse<CliqueToISResult>> compute(@RequestBody CliqueToISInput input) {
        CliqueToISResult result = service.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", result.getSourceProblem() + "_TO_" + result.getTargetProblem(),
                "executionTimeNanos", result.getExecutionTimeNanos(),
                "independentSetSize", result.getIndependentSetSize(),
                "operationCount", result.getOperationCount()
        );

        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }
}
