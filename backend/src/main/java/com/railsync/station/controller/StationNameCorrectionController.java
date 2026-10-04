package com.railsync.station.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.station.dto.StationCorrectionRequest;
import com.railsync.station.dto.StationCorrectionResponse;
import com.railsync.station.service.StationNameCorrectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/m3/stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StationNameCorrectionController {

    private final StationNameCorrectionService stationNameCorrectionService;

    @PostMapping("/correct")
    public ResponseEntity<ApiResponse<StationCorrectionResponse>> correctStationName(@RequestBody StationCorrectionRequest request) {
        StationCorrectionResponse response = stationNameCorrectionService.correctStationName(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
