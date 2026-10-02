package com.railsync.algorithm.m4.konig.controller;

import com.railsync.algorithm.m4.konig.dto.KonigInput;
import com.railsync.algorithm.m4.konig.dto.KonigResponseDto;
import com.railsync.algorithm.m4.konig.service.KonigService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m4/konig")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KonigController {

    private final KonigService konigService;

    @PostMapping
    public ResponseEntity<ApiResponse<KonigResponseDto>> compute(@RequestBody KonigInput input) {
        KonigResponseDto response = konigService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "matchingSize", response.getMatchingSize(),
                "vertexCoverSize", response.getVertexCoverSize(),
                "sizesEqual", response.isSizesEqual(),
                "operationCount", response.getOperationCount(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
