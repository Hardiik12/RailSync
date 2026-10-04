package com.railsync.dataimport;

import com.railsync.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/data-import")
@RequiredArgsConstructor
public class DataImportController {

    private final StationImporter stationImporter;

    @PostMapping(value = "/stations/geojson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImportResult>> importStations(
            @RequestPart("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("GeoJSON file is required");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".geojson")) {
            throw new IllegalArgumentException("Only .geojson files are accepted");
        }

        try {
            Path tempFile = Files.createTempFile("railsync-stations-", ".geojson");
            try {
                file.transferTo(tempFile);
                ImportResult result = stationImporter.importGeoJson(tempFile);
                return ResponseEntity.ok(ApiResponse.success(result));
            } finally {
                Files.deleteIfExists(tempFile);
            }
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException illegalArgumentException) {
                throw illegalArgumentException;
            }
            throw new IllegalArgumentException("Unable to import station dataset: " + e.getMessage(), e);
        }
    }
}
