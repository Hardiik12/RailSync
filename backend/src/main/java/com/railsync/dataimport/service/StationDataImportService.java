package com.railsync.dataimport.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.dataimport.dto.ImportResult;
import com.railsync.dataimport.dto.InspectionReport;
import com.railsync.dataimport.dto.RawStationRecord;
import com.railsync.dataimport.inspector.DatasetInspector;
import com.railsync.dataimport.normalizer.StationNormalizer;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class StationDataImportService {

    private final StationRepository stationRepository;
    private final ObjectMapper objectMapper;
    private final DatasetInspector datasetInspector;
    private final StationNormalizer stationNormalizer;

    public StationDataImportService(StationRepository stationRepository, ObjectMapper objectMapper) {
        this(stationRepository, objectMapper, new DatasetInspector(), new StationNormalizer());
    }

    public StationDataImportService(StationRepository stationRepository,
                                    ObjectMapper objectMapper,
                                    DatasetInspector datasetInspector,
                                    StationNormalizer stationNormalizer) {
        this.stationRepository = stationRepository;
        this.objectMapper = objectMapper;
        this.datasetInspector = datasetInspector != null ? datasetInspector : new DatasetInspector();
        this.stationNormalizer = stationNormalizer != null ? stationNormalizer : new StationNormalizer();
    }

    @Transactional
    public ImportResult importRawStations(List<RawStationRecord> rawRecords, String sourceDataset) {
        long startTime = System.currentTimeMillis();
        if (rawRecords == null || rawRecords.isEmpty()) {
            return ImportResult.builder()
                    .source(sourceDataset != null ? sourceDataset : "PUBLIC_DATASET")
                    .recordsRead(0)
                    .recordsInserted(0)
                    .recordsUpdated(0)
                    .duplicates(0)
                    .invalidRecords(0)
                    .durationMillis(0)
                    .build();
        }

        InspectionReport inspection = datasetInspector.inspectStations(rawRecords);

        int recordsInserted = 0;
        int recordsUpdated = 0;
        int duplicates = 0;
        int invalidRecords = 0;

        Set<String> processedBatchCodes = new HashSet<>();

        for (RawStationRecord raw : rawRecords) {
            if (raw == null || raw.getStationCode() == null || raw.getStationCode().isBlank() ||
                raw.getName() == null || raw.getName().isBlank() ||
                raw.getLatitude() == null || raw.getLongitude() == null ||
                raw.getLatitude() < -90.0 || raw.getLatitude() > 90.0 ||
                raw.getLongitude() < -180.0 || raw.getLongitude() > 180.0) {
                invalidRecords++;
                continue;
            }

            String codeKey = raw.getStationCode().trim().toUpperCase();

            if (processedBatchCodes.contains(codeKey)) {
                duplicates++;
                continue;
            }
            processedBatchCodes.add(codeKey);

            Station normalized = stationNormalizer.normalize(raw, sourceDataset);
            Optional<Station> existingOpt = stationRepository.findByStationCode(codeKey);

            if (existingOpt.isPresent()) {
                Station existing = existingOpt.get();
                existing.setName(normalized.getName());
                existing.setCity(normalized.getCity());
                existing.setState(normalized.getState());
                existing.setPlatformCount(normalized.getPlatformCount());
                existing.setStatus(normalized.getStatus());
                existing.setDataOrigin("PUBLIC_DATA");
                existing.setSourceDataset(normalized.getSourceDataset());
                existing.setLatitude(normalized.getLatitude());
                existing.setLongitude(normalized.getLongitude());
                stationRepository.save(existing);
                recordsUpdated++;
            } else {
                stationRepository.save(normalized);
                recordsInserted++;
            }
        }

        long durationMillis = System.currentTimeMillis() - startTime;
        return ImportResult.builder()
                .source(sourceDataset != null ? sourceDataset : "PUBLIC_DATASET")
                .recordsRead(rawRecords.size())
                .recordsInserted(recordsInserted)
                .recordsUpdated(recordsUpdated)
                .duplicates(duplicates)
                .invalidRecords(invalidRecords)
                .warnings(inspection.getWarnings())
                .errors(inspection.getErrors())
                .durationMillis(durationMillis)
                .build();
    }

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

        List<RawStationRecord> rawRecords = new ArrayList<>();
        for (JsonNode feature : featuresNode) {
            JsonNode properties = feature.get("properties");
            JsonNode geometry = feature.get("geometry");

            if (properties == null || geometry == null) {
                rawRecords.add(null);
                continue;
            }

            String stationCode = extractProperty(properties, "stationCode", "STATION_CODE", "code", "STATION_CD");
            String name = extractProperty(properties, "name", "STATION_NAME", "name_en");
            String city = extractProperty(properties, "city", "CITY");
            String state = extractProperty(properties, "state", "STATE");
            String status = extractProperty(properties, "status", "STATUS");
            Integer platformCount = extractIntProperty(properties, "platformCount", "PLATFORM_COUNT", "platforms");
            String sourceDataset = extractProperty(properties, "sourceDataset", "SOURCE_DATASET", "source");

            Double longitude = null;
            Double latitude = null;
            if (geometry.has("coordinates") && geometry.get("coordinates").isArray()) {
                JsonNode coords = geometry.get("coordinates");
                if (coords.size() >= 2 && coords.get(0).isNumber() && coords.get(1).isNumber()) {
                    longitude = coords.get(0).asDouble();
                    latitude = coords.get(1).asDouble();
                }
            }

            rawRecords.add(RawStationRecord.builder()
                    .stationCode(stationCode)
                    .name(name)
                    .city(city)
                    .state(state)
                    .status(status)
                    .platformCount(platformCount)
                    .sourceDataset(sourceDataset)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build());
        }

        ImportResult result = importRawStations(rawRecords, "PUBLIC_GEOJSON");
        result.setDurationMillis(System.currentTimeMillis() - startTime);
        return result;
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
