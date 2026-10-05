package com.railsync.train.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.train.dto.TrainDto;
import com.railsync.train.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TrainDto>>> getTrains(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "trainNumber") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Page<TrainDto> trains = trainService.getTrains(search, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(trains));
    }

    @GetMapping("/{trainNumber}")
    public ResponseEntity<ApiResponse<TrainDto>> getTrainByNumber(@PathVariable String trainNumber) {
        TrainDto train = trainService.getTrainByNumber(trainNumber);
        return ResponseEntity.ok(ApiResponse.success(train));
    }
}
