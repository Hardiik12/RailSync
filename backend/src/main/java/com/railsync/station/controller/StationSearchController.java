package com.railsync.station.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.station.dto.StationSearchRequest;
import com.railsync.station.dto.StationSearchResponse;
import com.railsync.station.service.StationSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/m1/stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StationSearchController {

    private final StationSearchService stationSearchService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<StationSearchResponse>> searchStations(@RequestBody StationSearchRequest request) {
        StationSearchResponse response = stationSearchService.searchStations(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
