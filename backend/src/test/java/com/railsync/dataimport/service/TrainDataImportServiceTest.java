package com.railsync.dataimport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.dataimport.dto.ImportResult;
import com.railsync.dataimport.dto.RawStopRecord;
import com.railsync.dataimport.dto.RawTrainRecord;
import com.railsync.dataimport.normalizer.TrainNormalizer;
import com.railsync.station.entity.Station;
import com.railsync.station.repository.StationRepository;
import com.railsync.train.entity.Route;
import com.railsync.train.entity.Train;
import com.railsync.train.repository.RouteRepository;
import com.railsync.train.repository.RouteStopRepository;
import com.railsync.train.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainDataImportServiceTest {

    @Mock
    private TrainRepository trainRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private RouteStopRepository routeStopRepository;

    private TrainDataImportService importService;

    @BeforeEach
    void setUp() {
        importService = new TrainDataImportService(
                trainRepository,
                stationRepository,
                routeRepository,
                routeStopRepository,
                new TrainNormalizer(),
                new ObjectMapper()
        );
    }

    @Test
    @DisplayName("Successfully process raw train records with route stops and station resolution")
    void testTrainImportSuccess() {
        Station ndls = Station.builder().id(1L).stationCode("NDLS").name("New Delhi").build();
        Station csmt = Station.builder().id(2L).stationCode("CSMT").name("Mumbai CSMT").build();

        when(stationRepository.findByStationCode("NDLS")).thenReturn(Optional.of(ndls));
        when(stationRepository.findByStationCode("CSMT")).thenReturn(Optional.of(csmt));
        when(trainRepository.findByTrainNumber("12951")).thenReturn(Optional.empty());

        Train savedTrain = Train.builder().id(10L).trainNumber("12951").trainName("Rajdhani").sourceStation(ndls).destinationStation(csmt).build();
        when(trainRepository.save(any(Train.class))).thenReturn(savedTrain);

        Route savedRoute = Route.builder().id(20L).train(savedTrain).build();
        when(routeRepository.findByTrainId(10L)).thenReturn(Collections.emptyList());
        when(routeRepository.save(any(Route.class))).thenReturn(savedRoute);

        RawTrainRecord raw = RawTrainRecord.builder()
                .trainNumber("12951")
                .trainName("Rajdhani Express")
                .sourceStationCode("NDLS")
                .destinationStationCode("CSMT")
                .distanceKm(1384.0)
                .stops(List.of(
                        RawStopRecord.builder().stationCode("NDLS").sequenceOrder(1).arrivalTime("16:55").departureTime("16:55").distanceKm(0.0).build(),
                        RawStopRecord.builder().stationCode("CSMT").sequenceOrder(2).arrivalTime("08:35").departureTime("08:35").distanceKm(1384.0).build()
                ))
                .build();

        ImportResult result = importService.importRawTrains(List.of(raw), "TEST_DATASET");

        assertThat(result.getRecordsRead()).isEqualTo(1);
        assertThat(result.getRecordsInserted()).isEqualTo(1);
        assertThat(result.getInvalidRecords()).isEqualTo(0);
        assertThat(result.getDuplicates()).isEqualTo(0);

        verify(trainRepository, times(1)).save(any(Train.class));
        verify(routeRepository, times(1)).save(any(Route.class));
        verify(routeStopRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Report error when source station is unknown")
    void testUnknownSourceStationError() {
        when(stationRepository.findByStationCode("UNKNOWN_SRC")).thenReturn(Optional.empty());

        RawTrainRecord raw = RawTrainRecord.builder()
                .trainNumber("99999")
                .trainName("Ghost Train")
                .sourceStationCode("UNKNOWN_SRC")
                .destinationStationCode("CSMT")
                .build();

        ImportResult result = importService.importRawTrains(List.of(raw), "TEST_DATASET");

        assertThat(result.getRecordsRead()).isEqualTo(1);
        assertThat(result.getRecordsInserted()).isEqualTo(0);
        assertThat(result.getInvalidRecords()).isEqualTo(1);
        assertThat(result.getErrors()).hasSize(1);
    }
}
