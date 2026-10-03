package com.railsync.algorithm.m5.approx;

import com.railsync.algorithm.m5.common.VertexCoverApproxInput;
import com.railsync.algorithm.m5.common.VertexCoverApproxResult;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m5/vertex-cover-2approx")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VertexCoverApproxController {

    private final VertexCoverApproxService service;

    @PostMapping
    public ResponseEntity<ApiResponse<VertexCoverApproxResult>> compute(@RequestBody VertexCoverApproxInput input) {
        VertexCoverApproxResult result = service.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", result.getAlgorithm(),
                "executionTimeNanos", result.getExecutionTimeNanos(),
                "coverSize", result.getCoverSize(),
                "operationCount", result.getOperationCount()
        );

        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }
}
