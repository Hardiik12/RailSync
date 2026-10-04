package com.railsync.dataimport.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.dataimport.dto.ImportResult;
import com.railsync.dataimport.service.StationDataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-import")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DataImportController {

    private final StationDataImportService stationDataImportService;

    @PostMapping("/stations/geojson")
    public ResponseEntity<ApiResponse<ImportResult>> importGeoJsonStations(@RequestBody String geoJsonContent) {
        ImportResult result = stationDataImportService.importGeoJsonStations(geoJsonContent);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
