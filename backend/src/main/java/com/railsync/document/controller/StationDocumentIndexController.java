package com.railsync.document.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.document.dto.StationIndexResponse;
import com.railsync.document.service.StationDocumentIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/m2/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StationDocumentIndexController {

    private final StationDocumentIndexService stationDocumentIndexService;

    @PostMapping("/index-stations")
    public ResponseEntity<ApiResponse<StationIndexResponse>> indexStationDocuments() {
        StationIndexResponse response = stationDocumentIndexService.indexStationDocuments();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
