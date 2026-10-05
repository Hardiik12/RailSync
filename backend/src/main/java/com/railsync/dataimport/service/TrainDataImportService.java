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
import com.railsync.train.entity.*;
import com.railsync.train.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainDataImportService {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final TripRepository tripRepository;
    private final StopTimeRepository stopTimeRepository;
    private final TrainNormalizer trainNormalizer;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportResult importTrainJson(String jsonContent) {
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

        int tripsRead = 0;
        int tripsCreated = 0;
        int tripsUpdated = 0;
        int stopTimesRead = 0;
        int stopTimesCreated = 0;
        int stopTimesUpdated = 0;

        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> processedBatchNumbers = new HashSet<>();
        LocalDate defaultServiceDate = LocalDate.now();

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

                // Trip and StopTimes timetable processing
                tripsRead++;
                Optional<Trip> tripOpt = tripRepository.findByTrainIdAndServiceDate(savedTrain.getId(), defaultServiceDate);
                Trip trip;
                if (tripOpt.isPresent()) {
                    trip = tripOpt.get();
                    tripsUpdated++;
                } else {
                    trip = Trip.builder()
                            .train(savedTrain)
                            .serviceDate(defaultServiceDate)
                            .scheduledStatus("SCHEDULED")
                            .build();
                    trip = tripRepository.save(trip);
                    tripsCreated++;
                }

                List<StopTime> stopTimesToSave = new ArrayList<>();
                for (RawStopRecord stopRaw : raw.getStops()) {
                    if (stopRaw == null || stopRaw.getStationCode() == null || stopRaw.getSequenceOrder() == null) {
                        continue;
                    }
                    stopTimesRead++;
                    String stCode = stopRaw.getStationCode().trim().toUpperCase();
                    Optional<Station> stOpt = stationRepository.findByStationCode(stCode);
                    if (stOpt.isEmpty()) {
                        continue;
                    }

                    Optional<StopTime> existingStOpt = stopTimeRepository.findByTripIdAndStopSequence(trip.getId(), stopRaw.getSequenceOrder());
                    StopTime st;
                    if (existingStOpt.isPresent()) {
                        st = existingStOpt.get();
                        st.setStation(stOpt.get());
                        st.setScheduledArrival(stopRaw.getArrivalTime());
                        st.setScheduledDeparture(stopRaw.getDepartureTime());
                        stopTimesUpdated++;
                    } else {
                        st = StopTime.builder()
                                .trip(trip)
                                .station(stOpt.get())
                                .stopSequence(stopRaw.getSequenceOrder())
                                .scheduledArrival(stopRaw.getArrivalTime())
                                .scheduledDeparture(stopRaw.getDepartureTime())
                                .build();
                        stopTimesCreated++;
                    }
                    stopTimesToSave.add(st);
                }

                if (!stopTimesToSave.isEmpty()) {
                    stopTimeRepository.saveAll(stopTimesToSave);
                }
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
                .tripsRead(tripsRead)
                .tripsCreated(tripsCreated)
                .tripsUpdated(tripsUpdated)
                .stopTimesRead(stopTimesRead)
                .stopTimesCreated(stopTimesCreated)
                .stopTimesUpdated(stopTimesUpdated)
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

        Map<Integer, RouteStop> existingStopsMap = new HashMap<>();
        List<RouteStop> currentRouteStops = routeStopRepository.findByRouteIdOrderByStopSequenceAsc(route.getId());
        for (RouteStop rs : currentRouteStops) {
            existingStopsMap.put(rs.getStopSequence(), rs);
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

            Integer calculatedDwell = calculateDwellMinutes(stopRaw.getArrivalTime(), stopRaw.getDepartureTime());

            RouteStop existingStop = existingStopsMap.get(stopRaw.getSequenceOrder());
            RouteStop stop;
            if (existingStop != null) {
                stop = existingStop;
                stop.setStation(stOpt.get());
                stop.setDistanceFromOrigin(stopRaw.getDistanceKm());
                stop.setScheduledArrival(stopRaw.getArrivalTime());
                stop.setScheduledDeparture(stopRaw.getDepartureTime());
                stop.setScheduledDwellMinutes(calculatedDwell);
            } else {
                stop = RouteStop.builder()
                        .route(route)
                        .station(stOpt.get())
                        .stopSequence(stopRaw.getSequenceOrder())
                        .distanceFromOrigin(stopRaw.getDistanceKm())
                        .scheduledArrival(stopRaw.getArrivalTime())
                        .scheduledDeparture(stopRaw.getDepartureTime())
                        .scheduledDwellMinutes(calculatedDwell)
                        .build();
            }
            stopsList.add(stop);
        }

        if (!stopsList.isEmpty()) {
            routeStopRepository.saveAll(stopsList);
        }
    }

    public static Integer calculateDwellMinutes(String arrivalTime, String departureTime) {
        if (arrivalTime == null || departureTime == null || arrivalTime.isBlank() || departureTime.isBlank()) {
            return null;
        }
        try {
            String[] arrParts = arrivalTime.trim().split(":");
            String[] depParts = departureTime.trim().split(":");
            if (arrParts.length < 2 || depParts.length < 2) {
                return null;
            }
            int arrMin = Integer.parseInt(arrParts[0]) * 60 + Integer.parseInt(arrParts[1]);
            int depMin = Integer.parseInt(depParts[0]) * 60 + Integer.parseInt(depParts[1]);
            int dwell = depMin - arrMin;
            if (dwell < 0) {
                dwell += 1440;
            }
            return dwell;
        } catch (Exception e) {
            return null;
        }
    }
}
