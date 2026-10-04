package com.railsync.dataimport.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.dataimport.dto.ImportResult;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationDataImportService {

    private final StationRepository stationRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportResult importGeoJsonStations(String geoJsonContent) {
        long startTime = System.currentTimeMillis();
        if (geoJsonContent == null || geoJsonContent.trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "GeoJSON payload cannot be empty");
        }

        JsonNode root;
        try {
            root = objectMapper.readTree(geoJsonContent);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Invalid GeoJSON JSON payload: " + e.getMessage());
        }

        JsonNode featuresNode = root.get("features");
        if (featuresNode == null || !featuresNode.isArray()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "GeoJSON must contain a 'features' array");
        }

        int recordsRead = featuresNode.size();
        int recordsInserted = 0;
        int recordsUpdated = 0;
        int duplicates = 0;
        int invalidRecords = 0;

        Set<String> processedBatchCodes = new HashSet<>();

        for (JsonNode feature : featuresNode) {
            JsonNode properties = feature.get("properties");
            JsonNode geometry = feature.get("geometry");

            if (properties == null || geometry == null) {
                invalidRecords++;
                continue;
            }

            String stationCode = extractProperty(properties, "stationCode", "STATION_CODE", "code", "STATION_CD");
            String name = extractProperty(properties, "name", "STATION_NAME", "name_en");
            String city = extractProperty(properties, "city", "CITY");
            if (city == null || city.isBlank()) city = (name != null ? name : "UNKNOWN");
            String state = extractProperty(properties, "state", "STATE");
            if (state == null || state.isBlank()) state = "UNKNOWN";

            String status = extractProperty(properties, "status", "STATUS");
            if (status == null || status.isBlank()) status = "ACTIVE";

            Integer platformCount = extractIntProperty(properties, "platformCount", "PLATFORM_COUNT", "platforms");
            if (platformCount == null || platformCount <= 0) platformCount = 1;

            String sourceDataset = extractProperty(properties, "sourceDataset", "SOURCE_DATASET", "source");
            if (sourceDataset == null || sourceDataset.isBlank()) sourceDataset = "PUBLIC_GEOJSON";

            // Geometry coordinates: Point [longitude, latitude]
            Double longitude = null;
            Double latitude = null;
            if (geometry.has("coordinates") && geometry.get("coordinates").isArray()) {
                JsonNode coords = geometry.get("coordinates");
                if (coords.size() >= 2 && coords.get(0).isNumber() && coords.get(1).isNumber()) {
                    longitude = coords.get(0).asDouble();
                    latitude = coords.get(1).asDouble();
                }
            }

            // Validate mandatory fields
            if (stationCode == null || stationCode.isBlank() || name == null || name.isBlank() ||
                latitude == null || longitude == null ||
                latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
                invalidRecords++;
                continue;
            }

            String codeKey = stationCode.trim().toUpperCase();

            // Check intra-batch duplicate
            if (processedBatchCodes.contains(codeKey)) {
                duplicates++;
                continue;
            }
            processedBatchCodes.add(codeKey);

            // Database lookup
            Optional<Station> existingOpt = stationRepository.findByStationCode(codeKey);
            if (existingOpt.isPresent()) {
                Station existing = existingOpt.get();
                existing.setName(name.trim());
                existing.setCity(city.trim());
                existing.setState(state.trim());
                existing.setPlatformCount(platformCount);
                existing.setStatus(status.trim().toUpperCase());
                existing.setDataOrigin("PUBLIC_DATA");
                existing.setSourceDataset(sourceDataset.trim());
                existing.setLatitude(latitude);
                existing.setLongitude(longitude);
                stationRepository.save(existing);
                recordsUpdated++;
            } else {
                Station newStation = Station.builder()
                        .stationCode(codeKey)
                        .name(name.trim())
                        .city(city.trim())
                        .state(state.trim())
                        .platformCount(platformCount)
                        .status(status.trim().toUpperCase())
                        .dataOrigin("PUBLIC_DATA")
                        .sourceDataset(sourceDataset.trim())
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();
                stationRepository.save(newStation);
                recordsInserted++;
            }
        }

        long durationMillis = System.currentTimeMillis() - startTime;
        return ImportResult.builder()
                .recordsRead(recordsRead)
                .recordsInserted(recordsInserted)
                .recordsUpdated(recordsUpdated)
                .duplicates(duplicates)
                .invalidRecords(invalidRecords)
                .durationMillis(durationMillis)
                .build();
    }

    private String extractProperty(JsonNode props, String... keys) {
        for (String key : keys) {
            if (props.has(key) && !props.get(key).isNull()) {
                String val = props.get(key).asText();
                if (val != null && !val.isBlank()) {
                    return val;
                }
            }
        }
        return null;
    }

    private Integer extractIntProperty(JsonNode props, String... keys) {
        for (String key : keys) {
            if (props.has(key) && props.get(key).isNumber()) {
                return props.get(key).asInt();
            }
        }
        return null;
    }
}
