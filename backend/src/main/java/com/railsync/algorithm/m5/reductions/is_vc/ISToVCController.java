package com.railsync.algorithm.m5.reductions.is_vc;

import com.railsync.algorithm.m5.common.ISToVCInput;
import com.railsync.algorithm.m5.common.ISToVCResult;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m5/independent-set-to-vertex-cover")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ISToVCController {

    private final ISToVCService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ISToVCResult>> compute(@RequestBody ISToVCInput input) {
        ISToVCResult result = service.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", result.getSourceProblem() + "_TO_" + result.getTargetProblem(),
                "executionTimeNanos", result.getExecutionTimeNanos(),
                "vertexCoverSize", result.getVertexCoverSize(),
                "operationCount", result.getOperationCount()
        );

        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }
}
