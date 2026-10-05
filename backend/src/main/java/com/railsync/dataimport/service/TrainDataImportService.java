package com.railsync.dataimport.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import com.railsync.dataimport.dto.ImportResult;
import com.railsync.dataimport.dto.RawStopRecord;
import com.railsync.dataimport.dto.RawTrainRecord;
import com.railsync.dataimport.normalizer.TrainNormalizer;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import com.railsync.train.entity.Route;
import com.railsync.train.entity.RouteStop;
import com.railsync.train.entity.Train;
import com.railsync.train.repository.RouteRepository;
import com.railsync.train.repository.RouteStopRepository;
import com.railsync.train.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainDataImportService {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final TrainNormalizer trainNormalizer;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportResult importTrainJson(String jsonContent) {
        long startTime = System.currentTimeMillis();
        if (jsonContent == null || jsonContent.trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Train JSON payload cannot be empty");
        }

        List<RawTrainRecord> records;
        try {
            records = objectMapper.readValue(jsonContent, new TypeReference<List<RawTrainRecord>>() {});
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Failed to parse train JSON: " + e.getMessage());
        }

        return importRawTrains(records, "PUBLIC_TRAIN_DATASET");
    }

    @Transactional
    public ImportResult importRawTrains(List<RawTrainRecord> rawRecords, String sourceDataset) {
        long startTime = System.currentTimeMillis();
        if (rawRecords == null || rawRecords.isEmpty()) {
            return ImportResult.builder()
                    .source(sourceDataset != null ? sourceDataset : "PUBLIC_TRAIN_DATASET")
                    .recordsRead(0)
                    .recordsInserted(0)
                    .recordsUpdated(0)
                    .durationMillis(0)
                    .build();
        }

        int recordsRead = rawRecords.size();
        int recordsInserted = 0;
        int recordsUpdated = 0;
        int duplicates = 0;
        int invalidRecords = 0;

        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> processedBatchNumbers = new HashSet<>();

        for (int i = 0; i < rawRecords.size(); i++) {
            RawTrainRecord raw = rawRecords.get(i);
            if (raw == null || raw.getTrainNumber() == null || raw.getTrainNumber().isBlank()) {
                invalidRecords++;
                errors.add("Record index " + i + " is missing trainNumber");
                continue;
            }

            String trainNumKey = raw.getTrainNumber().trim().toUpperCase();

            if (processedBatchNumbers.contains(trainNumKey)) {
                duplicates++;
                warnings.add("Duplicate trainNumber in batch: " + trainNumKey);
                continue;
            }
            processedBatchNumbers.add(trainNumKey);

            if (raw.getSourceStationCode() == null || raw.getDestinationStationCode() == null) {
                invalidRecords++;
                errors.add("Train " + trainNumKey + " missing source/destination station code");
                continue;
            }

            String srcCode = raw.getSourceStationCode().trim().toUpperCase();
            String destCode = raw.getDestinationStationCode().trim().toUpperCase();

            Optional<Station> srcOpt = stationRepository.findByStationCode(srcCode);
            Optional<Station> destOpt = stationRepository.findByStationCode(destCode);

            if (srcOpt.isEmpty()) {
                invalidRecords++;
                errors.add("Unknown source station code '" + srcCode + "' for train " + trainNumKey);
                continue;
            }

            if (destOpt.isEmpty()) {
                invalidRecords++;
                errors.add("Unknown destination station code '" + destCode + "' for train " + trainNumKey);
                continue;
            }

            Station srcStation = srcOpt.get();
            Station destStation = destOpt.get();

            Train trainEntity = trainNormalizer.normalize(raw, srcStation, destStation);
            Optional<Train> existingOpt = trainRepository.findByTrainNumber(trainNumKey);

            Train savedTrain;
            if (existingOpt.isPresent()) {
                Train existing = existingOpt.get();
                existing.setTrainName(trainEntity.getTrainName());
                existing.setTrainType(trainEntity.getTrainType());
                existing.setSourceStation(srcStation);
                existing.setDestinationStation(destStation);
                existing.setRunningDays(trainEntity.getRunningDays());
                existing.setDistanceKm(trainEntity.getDistanceKm());
                savedTrain = trainRepository.save(existing);
                recordsUpdated++;
            } else {
                savedTrain = trainRepository.save(trainEntity);
                recordsInserted++;
            }

            // Route and RouteStops processing
            if (raw.getStops() != null && !raw.getStops().isEmpty()) {
                processRouteAndStops(savedTrain, raw.getStops(), warnings);
            }
        }

        long durationMillis = System.currentTimeMillis() - startTime;
        return ImportResult.builder()
                .source(sourceDataset != null ? sourceDataset : "PUBLIC_TRAIN_DATASET")
                .recordsRead(recordsRead)
                .recordsInserted(recordsInserted)
                .recordsUpdated(recordsUpdated)
                .duplicates(duplicates)
                .invalidRecords(invalidRecords)
                .warnings(warnings)
                .errors(errors)
                .durationMillis(durationMillis)
                .build();
    }

    private void processRouteAndStops(Train train, List<RawStopRecord> rawStops, List<String> warnings) {
        List<Route> existingRoutes = routeRepository.findByTrainId(train.getId());
        Route route;
        if (!existingRoutes.isEmpty()) {
            route = existingRoutes.get(0);
        } else {
            route = Route.builder()
                    .train(train)
                    .routeName(train.getTrainNumber() + " Main Route")
                    .distanceKm(train.getDistanceKm())
                    .status("ACTIVE")
                    .build();
            route = routeRepository.save(route);
        }

        List<RouteStop> stopsList = new ArrayList<>();
        Set<Integer> sequences = new HashSet<>();

        for (RawStopRecord stopRaw : rawStops) {
            if (stopRaw == null || stopRaw.getStationCode() == null || stopRaw.getSequenceOrder() == null) {
                continue;
            }

            String stCode = stopRaw.getStationCode().trim().toUpperCase();
            Optional<Station> stOpt = stationRepository.findByStationCode(stCode);
            if (stOpt.isEmpty()) {
                warnings.add("Route stop skipped for train " + train.getTrainNumber() + ": Unknown station " + stCode);
                continue;
            }

            if (sequences.contains(stopRaw.getSequenceOrder())) {
                warnings.add("Duplicate stopSequence " + stopRaw.getSequenceOrder() + " for train " + train.getTrainNumber());
                continue;
            }
            sequences.add(stopRaw.getSequenceOrder());

            RouteStop stop = RouteStop.builder()
                    .route(route)
                    .station(stOpt.get())
                    .stopSequence(stopRaw.getSequenceOrder())
                    .distanceFromOrigin(stopRaw.getDistanceKm())
                    .scheduledArrival(stopRaw.getArrivalTime())
                    .scheduledDeparture(stopRaw.getDepartureTime())
                    .scheduledDwellMinutes(2)
                    .build();
            stopsList.add(stop);
        }

        if (!stopsList.isEmpty()) {
            routeStopRepository.saveAll(stopsList);
        }
    }
}
