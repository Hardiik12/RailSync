package com.railsync.station.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.station.dto.StationDto;
import com.railsync.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "stationCode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Map<String, Object> stationsData = stationService.getStations(page, size, search, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(stationsData));
    }

    @GetMapping("/{stationCode}")
    public ResponseEntity<ApiResponse<StationDto>> getStationByCode(@PathVariable String stationCode) {
        StationDto station = stationService.getStationByCode(stationCode);
        return ResponseEntity.ok(ApiResponse.success(station));
    }
}
