package com.railsync.algorithm.m2.lcp.controller;

import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResponseDto;
import com.railsync.algorithm.m2.kasai.service.KasaiService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m2/lcp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LCPController {

    private final KasaiService kasaiService;

    @PostMapping
    public ResponseEntity<ApiResponse<KasaiResponseDto>> compute(@RequestBody KasaiInput input) {
        // LCP calculation delegates directly to Kasai algorithm
        KasaiResponseDto response = kasaiService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", "LCP via " + response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "maxLcpValue", response.getMaxLcpValue(),
                "longestRepeatedSubstring", response.getLongestRepeatedSubstring(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
