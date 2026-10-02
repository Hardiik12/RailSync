package com.railsync.algorithm.m2.sais.controller;

import com.railsync.algorithm.m2.sais.dto.SAISInput;
import com.railsync.algorithm.m2.sais.dto.SAISResponseDto;
import com.railsync.algorithm.m2.sais.service.SAISService;
import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/m2/sa-is")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SAISController {

    private final SAISService saisService;

    @PostMapping
    public ResponseEntity<ApiResponse<SAISResponseDto>> compute(@RequestBody SAISInput input) {
        SAISResponseDto response = saisService.execute(input);

        Map<String, Object> meta = Map.of(
                "algorithm", response.getAlgorithm(),
                "executionTimeNanos", response.getExecutionTimeNanos(),
                "operationCount", response.getOperationCount(),
                "lmsCount", response.getLmsCount(),
                "recursionDepth", response.getRecursionDepth(),
                "traceEnabled", Boolean.TRUE.equals(input.getTraceEnabled())
        );

        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }
}
