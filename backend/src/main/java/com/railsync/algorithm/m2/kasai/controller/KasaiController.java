package com.railsync.algorithm.m2.kasai.controller;

import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResponseDto;
import com.railsync.algorithm.m2.kasai.service.KasaiService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m2/kasai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KasaiController {

    private final KasaiService kasaiService;

    @PostMapping
    public ResponseEntity<ApiResponse<KasaiResponseDto>> compute(@RequestBody KasaiInput input) {
        KasaiResponseDto response = kasaiService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "maxLcpValue", response.getMaxLcpValue(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
