package com.railsync.train.service;

import com.railsync.common.error.ApiException;
import com.railsync.station.entity.Station;
import com.railsync.train.dto.TrainDto;
import com.railsync.train.entity.Train;
import com.railsync.train.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    private TrainService trainService;

    @BeforeEach
    void setUp() {
        trainService = new TrainService(trainRepository);
    }

    @Test
    @DisplayName("Fetch train by number successfully")
    void testGetTrainByNumberSuccess() {
        Station ndls = Station.builder().stationCode("NDLS").name("New Delhi").city("Delhi").state("DL").platformCount(16).status("ACTIVE").build();
        Station csmt = Station.builder().stationCode("CSMT").name("Mumbai CSMT").city("Mumbai").state("MH").platformCount(18).status("ACTIVE").build();

        Train train = Train.builder()
                .id(1L)
                .trainNumber("12951")
                .trainName("Mumbai Rajdhani")
                .sourceStation(ndls)
                .destinationStation(csmt)
                .status("ACTIVE")
                .build();

        when(trainRepository.findByTrainNumber("12951")).thenReturn(Optional.of(train));

        TrainDto result = trainService.getTrainByNumber("12951");

        assertThat(result.getTrainNumber()).isEqualTo("12951");
        assertThat(result.getTrainName()).isEqualTo("Mumbai Rajdhani");
        assertThat(result.getSourceStation().getStationCode()).isEqualTo("NDLS");
    }

    @Test
    @DisplayName("Throw RESOURCE_NOT_FOUND exception for non-existent train")
    void testGetTrainByNumberNotFound() {
        when(trainRepository.findByTrainNumber("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainService.getTrainByNumber("UNKNOWN"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Train not found");
    }
}
